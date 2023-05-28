package com.tomasrepcik.blumodify.bluetooth.viewmodel

import app.cash.turbine.test
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.notifications.NotificationRepoTemplate
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManagerTemplate
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.reset
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class BluModifyViewModelTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val androidLogs = AndroidLogMockRule()

    @Mock
    private var btWorkManager: BtWorkManagerTemplate = mock()

    @Mock
    private var btController: BtControllerTemplate = mock()

    @Mock
    private var btDeviceDao: BtDeviceDao = mock()

    @Mock
    private var notificationRepo: NotificationRepoTemplate = mock()

    private var sut: BluModifyViewModel =
        BluModifyViewModel(btWorkManager, btController, btDeviceDao, notificationRepo)


    @Before
    fun setUp() {
        sut = BluModifyViewModel(btWorkManager, btController, btDeviceDao, notificationRepo)

    }

    @After
    fun tearDown() = reset(btController, btWorkManager, btDeviceDao, notificationRepo)

    @Test
    fun `Given the sut is initialized, then the service is in Loading state`() =
        assertTrue(sut.blumodifyState.value == BluModifyState.Loading)

    @Test
    fun `Given the service is running - When the OnLaunch is called, then the service is TurnedOn`() =
        runTest {
            // ARRANGE
            btWorkManager.stub {
                onBlocking { this.workersWork() } doAnswer { AppResult.Success(true) }
            }

            sut.blumodifyState.test {

                // ACTION
                sut.onEvent(BluModifyEvent.OnLaunch)

                // CHECK
                assertEquals(BluModifyState.Loading, awaitItem())
                assertEquals(BluModifyState.TurnedOn, awaitItem())
            }
        }

    @Test
    fun `Given the service is not running - When the OnLaunch is called, then the service is TurnedOff`() =
        runTest {
            // ARRANGE
            btWorkManager.stub {
                onBlocking { this.workersWork() } doAnswer { AppResult.Success(false) }
            }

            sut.blumodifyState.test {

                // ACTION
                sut.onEvent(BluModifyEvent.OnLaunch)

                // CHECK
                assertEquals(BluModifyState.Loading, awaitItem())
                assertEquals(BluModifyState.TurnedOff, awaitItem())
            }
        }

    @Test
    fun `Given the service failed - When the OnLaunch is called, then the service is in Error state`() =
        runTest {
            // ARRANGE
            val error = AppResult.Error<Boolean>(
                message = "error", origin = "origin", errorCause = ErrorCause.WORKER_NOT_FOUND
            )
            btWorkManager.stub {
                onBlocking { this.workersWork() } doAnswer { error }
            }

            sut.blumodifyState.test {

                // ACTION
                sut.onEvent(BluModifyEvent.OnLaunch)

                // CHECK
                assertEquals(BluModifyState.Loading, awaitItem())

                val state = awaitItem()
                assertTrue(state is BluModifyState.ErrorOccurred)
                state as BluModifyState.ErrorOccurred
                assertEquals(state.error, error)
            }
        }

    @Test
    fun `Given the service failed - When the retry button is clicked, then the service tries to reset the service, but error persists`() =
        runTest {
            // ARRANGE
            val error = AppResult.Error<Boolean>(
                message = "error", origin = "origin", errorCause = ErrorCause.WORKER_NOT_FOUND
            )
            btWorkManager.stub {
                onBlocking { this.workersWork() } doAnswer { error }
            }
            btController.stub {
                on(it.isPermission()) doReturn (true)
            }
            notificationRepo.stub {
                on(it.isPermission()) doReturn (true)
            }
            btDeviceDao.stub {
                onBlocking { this.resetDevices() } doAnswer {}
            }

            sut.blumodifyState.test {

                sut.onEvent(BluModifyEvent.OnLaunch)
                // Loading
                assertEquals(BluModifyState.Loading, awaitItem())

                // Error check
                val errorStateBefore = awaitItem()
                assertTrue(errorStateBefore is BluModifyState.ErrorOccurred)
                errorStateBefore as BluModifyState.ErrorOccurred
                assertEquals(errorStateBefore.error, error)

                // ACTION
                sut.onEvent(BluModifyEvent.OnError)

                // CHECK
                val errorStateDueReset = awaitItem()
                assertTrue(errorStateDueReset is BluModifyState.ErrorOccurred)
                val errorStateAfter = awaitItem()
                assertTrue(errorStateAfter is BluModifyState.ErrorOccurred)
                errorStateAfter as BluModifyState.ErrorOccurred
                assertEquals(errorStateAfter.error, error)

                verify(btWorkManager, times(1)).initWorkers()
                verify(btWorkManager, times(1)).disposeWorkers()
                verify(btDeviceDao, times(1)).resetDevices()
                verify(btController, times(1)).isPermission()
                verify(notificationRepo, times(1)).isPermission()

            }
        }

    @Test
    fun `Given the service is not running - When the main button is clicked, then the service turns up`() =
        runTest {
            // ARRANGE
            btWorkManager.stub {
                onBlocking { this.workersWork() } doAnswer { AppResult.Success(false) }
                onBlocking { this.initWorkers() } doAnswer {}
            }
            btController.stub {
                on(it.isPermission()) doReturn (true)
            }
            notificationRepo.stub {
                on(it.isPermission()) doReturn (true)
            }

            sut.blumodifyState.test {

                sut.onEvent(BluModifyEvent.OnLaunch)
                assertEquals(BluModifyState.Loading, awaitItem())
                assertEquals(BluModifyState.TurnedOff, awaitItem())
                btWorkManager.stub {
                    onBlocking { this.workersWork() } doAnswer { AppResult.Success(true) }
                }

                // ACTION
                sut.onEvent(BluModifyEvent.OnMainButtonClickEvent)

                // CHECK
                assertEquals(BluModifyState.TurnedOn, awaitItem())
                verify(btWorkManager, never()).disposeWorkers()
                verify(btDeviceDao, never()).resetDevices()
                verify(btWorkManager, times(1)).initWorkers()
            }
        }

    @Test
    fun `Given the service is running - When the main button is clicked, then the service turns off`() =
        runTest {
            // ARRANGE
            btWorkManager.stub {
                onBlocking { this.workersWork() } doAnswer { AppResult.Success(true) }
                onBlocking { this.disposeWorkers() } doAnswer {}
            }
            btController.stub {
                on(it.isPermission()) doReturn (true)
            }
            notificationRepo.stub {
                on(it.isPermission()) doReturn (true)
            }
            btDeviceDao.stub {
                onBlocking { this.resetDevices() } doAnswer {}
            }


            sut.blumodifyState.test {

                sut.onEvent(BluModifyEvent.OnLaunch)
                assertEquals(BluModifyState.Loading, awaitItem())
                assertEquals(BluModifyState.TurnedOn, awaitItem())
                btWorkManager.stub {
                    onBlocking { this.workersWork() } doAnswer { AppResult.Success(false) }
                }

                // ACTION
                sut.onEvent(BluModifyEvent.OnMainButtonClickEvent)

                // CHECK
                assertEquals(BluModifyState.TurnedOff, awaitItem())
                verify(btWorkManager, never()).initWorkers()
                verify(btWorkManager, times(1)).disposeWorkers()
                verify(btDeviceDao, times(1)).resetDevices()
                verify(btController, never()).isPermission()
                verify(notificationRepo, never()).isPermission()
            }
        }

    @Test
    fun `Given the service is not running and BT permission is missing - When the main button is clicked, then the request for permission is shown`() =
        runTest {
            // ARRANGE
            btWorkManager.stub {
                onBlocking { this.workersWork() } doAnswer { AppResult.Success(false) }
                onBlocking { this.initWorkers() } doAnswer {}
            }
            btController.stub {
                on(it.isPermission()) doReturn (false)
            }
            notificationRepo.stub {
                on(it.isPermission()) doReturn (true)
            }

            sut.blumodifyState.test {

                sut.onEvent(BluModifyEvent.OnLaunch)
                assertEquals(BluModifyState.Loading, awaitItem())
                assertEquals(BluModifyState.TurnedOff, awaitItem())

                // ACTION
                sut.onEvent(BluModifyEvent.OnMainButtonClickEvent)

                // CHECK
                assertEquals(BluModifyState.MissingPermission, awaitItem())
                verify(btWorkManager, never()).disposeWorkers()
                verify(btDeviceDao, never()).resetDevices()
                verify(btWorkManager, never()).initWorkers()
                verify(btController, times(1)).isPermission()
                verify(notificationRepo, never()).isPermission()
            }
        }

    @Test
    fun `Given the service is not running and Notification permission is missing - When the main button is clicked, then the request for permission is shown`() =
        runTest {
            // ARRANGE
            btWorkManager.stub {
                onBlocking { this.workersWork() } doAnswer { AppResult.Success(false) }
                onBlocking { this.initWorkers() } doAnswer {}
            }
            btController.stub {
                on(it.isPermission()) doReturn (true)
            }
            notificationRepo.stub {
                on(it.isPermission()) doReturn (false)
            }

            sut.blumodifyState.test {

                sut.onEvent(BluModifyEvent.OnLaunch)
                assertEquals(BluModifyState.Loading, awaitItem())
                assertEquals(BluModifyState.TurnedOff, awaitItem())

                // ACTION
                sut.onEvent(BluModifyEvent.OnMainButtonClickEvent)

                // CHECK
                assertEquals(BluModifyState.MissingPermission, awaitItem())
                verify(btWorkManager, never()).disposeWorkers()
                verify(btDeviceDao, never()).resetDevices()
                verify(btWorkManager, never()).initWorkers()
                verify(btController, times(1)).isPermission()
                verify(notificationRepo, times(1)).isPermission()
            }
        }
}