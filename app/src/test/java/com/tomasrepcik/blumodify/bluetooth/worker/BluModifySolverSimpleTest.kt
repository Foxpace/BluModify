package com.tomasrepcik.blumodify.bluetooth.worker

import androidx.work.ListenableWorker
import com.tomasrepcik.blumodify.app.notifications.NotificationRepoTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.app.time.TimeRepoTemplate
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.never
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyBlocking
@RunWith(JUnit4::class)
class BluModifySolverSimpleTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val androidLogs = AndroidLogMockRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var btController: BtControllerTemplate

    @Mock
    lateinit var btDeviceDao: BtDeviceDao

    @Mock
    lateinit var btLogsDao: LogsDao

    @Mock
    lateinit var appCache: AppCacheTemplate<AppCacheState>

    @Mock
    lateinit var notificationRepo: NotificationRepoTemplate

    @Mock
    lateinit var timeRepo: TimeRepoTemplate


    private lateinit var sut: BluModifySolverTemplate

    private val minusThreeDays = (-3 * 24 * 3600 * 1000).toLong()
    private val btDevice1 = BtItem(deviceName = "device 1", macAddress = "AA:AA")
    private val btDevice2 = BtItem(deviceName = "device 2", macAddress = "00:00")
    private val logReport = LogReport(
        startTime = 0L, connectedDevices = emptyList(), isSuccess = false, stackTrace = null
    )

    @Before
    fun setUp() {
        btController.stub {
            on(it.registerObserver(notificationRepo)) doAnswer {}
        }
        btLogsDao.stub {
            onBlocking { it.deleteOlderItemsThan(any()) } doAnswer {}
        }
        timeRepo.stub {
            on(it.currentMillis()) doAnswer { 0L }
        }
        sut = BluModifySolver(
            btController, btDeviceDao, btLogsDao, appCache, notificationRepo, timeRepo
        )
    }

    @Test
    fun `When the Bluetooth permission is missing, then returns failure`() = runTest {
        // ARRANGE
        btController.stub {
            on { it.isPermission() } doAnswer { false }
        }
        btLogsDao.stub {
            onBlocking {
                it.insertReport(logReport)
            } doAnswer {}
        }

        // ACTION
        val result = sut.onWorkerCall()

        // CHECK
        assertEquals(result, ListenableWorker.Result.failure())
        verify(btController, times(1)).isPermission()
        verify(btController, times(1)).registerObserver(notificationRepo)
        verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
        verify(btLogsDao, times(1)).insertReport(any())
        verify(notificationRepo, never()).isPermission()

    }

    @Test
    fun `When the BT permission is granted and BT is off, then return success result`() = runTest {
        // ARRANGE
        btController.stub {
            on { it.isPermission() } doAnswer { true }
            on { it.isBtOn() } doAnswer { false }
        }
        btLogsDao.stub {
            onBlocking {
                it.insertReport(logReport.copy(isSuccess = true))
            } doAnswer {}
        }

        // ACTION
        val result = sut.onWorkerCall()

        // CHECK
        assertEquals(ListenableWorker.Result.success(), result)
        verify(btController, times(1)).isPermission()
        verify(btController, times(1)).isBtOn()
        verify(btController, times(1)).registerObserver(notificationRepo)
        verify(notificationRepo, never()).isPermission()
        verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
        verify(btLogsDao, times(1)).insertReport(logReport.copy(isSuccess = true))
    }

    @Test
    fun `When the BT permission is granted and BT is on and notification permission is off, then return failure result`() =
        runTest {
            // ARRANGE
            btController.stub {
                on { it.isPermission() } doAnswer { true }
                on { it.isBtOn() } doAnswer { true }
            }
            notificationRepo.stub {
                on { it.isPermission() } doAnswer { false }
            }
            btLogsDao.stub {
                onBlocking {
                    it.insertReport(logReport)
                } doAnswer {}
            }

            // ACTION
            val result = sut.onWorkerCall()

            // CHECK
            assertEquals(ListenableWorker.Result.failure(), result)
            verify(btController, times(1)).isPermission()
            verify(btController, times(1)).isBtOn()
            verify(btController, times(1)).registerObserver(notificationRepo)
            verify(notificationRepo, times(1)).isPermission()
            verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
            verify(btLogsDao, times(1)).insertReport(any())
        }

    @Test
    fun `When all permissions are granted and BT is on and no device is connected, then return success result and call for notification`() =
        runTest {
            // ARRANGE
            btController.stub {
                on { it.isPermission() } doAnswer { true }
                on { it.isBtOn() } doAnswer { true }
                onBlocking { it.getConnectedBleDevices() } doAnswer { setOf() }
                onBlocking { it.getConnectedBtDevices() } doAnswer { setOf() }
            }
            notificationRepo.stub {
                on { it.isPermission() } doAnswer { true }
                onBlocking { it.postNotification(null) } doAnswer {}
            }
            appCache.stub {
                onBlocking { it.loadInCacheSync() } doAnswer { AppSettings.default }
            }
            btLogsDao.stub {
                onBlocking {
                    it.insertReport(logReport.copy(isSuccess = true))
                } doAnswer {}
            }

            // ACTION
            val result = sut.onWorkerCall()

            // CHECK
            assertEquals(ListenableWorker.Result.success(), result)
            verify(btController, times(1)).isPermission()
            verify(btController, times(1)).isBtOn()
            verify(btController, times(1)).registerObserver(notificationRepo)
            verify(notificationRepo, times(1)).isPermission()
            verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
            verify(btLogsDao, times(1)).insertReport(logReport.copy(isSuccess = true))

            verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
            verifyBlocking(btController, times(1)) { btController.getConnectedBleDevices() }
            verifyBlocking(notificationRepo, times(1)) { notificationRepo.postNotification(null) }

        }

    @Test
    fun `When all permissions are granted and BT is on and device is connected, then return success result and do not call notification`() =
        runTest {
            // ARRANGE
            btController.stub {
                on { it.isPermission() } doAnswer { true }
                on { it.isBtOn() } doAnswer { true }
                onBlocking { it.getConnectedBleDevices() } doAnswer { setOf(btDevice1) }
                onBlocking { it.getConnectedBtDevices() } doAnswer { setOf(btDevice2) }
            }
            notificationRepo.stub {
                on { it.isPermission() } doAnswer { true }
                onBlocking { it.postNotification(null) } doAnswer {}
            }
            appCache.stub {
                onBlocking { it.loadInCacheSync() } doAnswer { AppSettings.default }
            }
            btLogsDao.stub {
                onBlocking {
                    it.insertReport(logReport.copy(isSuccess = true))
                } doAnswer {}
            }

            // ACTION
            val result = sut.onWorkerCall()

            // CHECK
            assertEquals(ListenableWorker.Result.success(), result)
            verify(btController, times(1)).isPermission()
            verify(btController, times(1)).isBtOn()
            verify(btController, times(1)).registerObserver(notificationRepo)
            verify(notificationRepo, times(1)).isPermission()
            verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
            verify(btLogsDao, times(1)).insertReport(
                logReport.copy(
                    isSuccess = true, connectedDevices = listOf(btDevice1, btDevice2)
                )
            )

            verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
            verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
            verifyBlocking(appCache, times(1)) { appCache.loadInCacheSync() }
            verifyBlocking(notificationRepo, never()) { notificationRepo.postNotification(null) }

        }
}