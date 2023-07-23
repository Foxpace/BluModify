package com.tomasrepcik.blumodify.bluetooth.worker

import androidx.work.ListenableWorker
import com.tomasrepcik.blumodify.app.notifications.NotificationRepoTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.app.time.TimeRepoTemplate
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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
class BluModifySolverAdvancedTest {

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
    private val btNotConnectedDeviceInPast =
        BtDevice("AA:AA", "device 1", wasConnected = false, lastConnection = 0L)

    private val btDevice2 = BtItem(deviceName = "device 2", macAddress = "00:00")
    private val btConnectedDeviceInPast =
        BtDevice("00:00", "device 2", wasConnected = true, lastConnection = 0L)

    private val logReport = LogReport(
        startTime = 0L, connectedDevices = emptyList(), isSuccess = false, stackTrace = null
    )

    @Before
    fun setUp() {
        btController.stub {
            on(it.registerObserver(notificationRepo)) doAnswer {}
            on { it.isPermission() } doAnswer { true }
            on { it.isBtOn() } doAnswer { true }
        }
        btLogsDao.stub {
            onBlocking { it.deleteOlderItemsThan(any()) } doAnswer {}
        }
        notificationRepo.stub {
            on { it.isPermission() } doAnswer { true }
        }
        timeRepo.stub {
            on(it.currentMillis()) doAnswer { 0L }
        }
        appCache.stub {
            onBlocking { it.loadInCacheSync() } doAnswer {
                AppSettings.default.copy(
                    isAdvancedSettings = true
                )
            }
        }
        sut = BluModifySolver(
            btController, btDeviceDao, btLogsDao, appCache, notificationRepo, timeRepo
        )
    }

    @Test
    fun `When all permissions are granted and BT is on and no device was connected, then return success result and do not call notification`() = runTest {

        // ARRANGE
        btController.stub {
            onBlocking { it.getConnectedBleDevices() } doAnswer { setOf() }
            onBlocking { it.getConnectedBtDevices() } doAnswer { setOf() }
        }
        btDeviceDao.stub {
            onBlocking { it.getAll() } doAnswer {listOf(btNotConnectedDeviceInPast)}
        }

        // ACTION
        val result = sut.onWorkerCall()

        // CHECK
        Assert.assertEquals(ListenableWorker.Result.success(), result)
        verify(btController, times(1)).isPermission()
        verify(btController, times(1)).isBtOn()
        verify(btController, times(1)).registerObserver(notificationRepo)
        verify(notificationRepo, times(1)).isPermission()
        verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
        verify(btLogsDao, times(1)).insertReport(logReport.copy(isSuccess = true))
        verify(btDeviceDao, times(1)).getAll()

        verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
        verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
        verifyBlocking(appCache, times(1)) { appCache.loadInCacheSync() }
        verifyBlocking(notificationRepo, never()) { notificationRepo.postNotification(null) }
    }

    @Test
    fun `When all permissions are granted and BT is on and device was connected, but it is not now connected, then returns success result and calls notification`() = runTest {

        // ARRANGE
        btController.stub {
            onBlocking { it.getConnectedBleDevices() } doAnswer { setOf() }
            onBlocking { it.getConnectedBtDevices() } doAnswer { setOf() }
        }
        btDeviceDao.stub {
            onBlocking { it.getAll() } doAnswer {listOf(btConnectedDeviceInPast)}
        }

        // ACTION
        val result = sut.onWorkerCall()

        // CHECK
        Assert.assertEquals(ListenableWorker.Result.success(), result)
        verify(btController, times(1)).isPermission()
        verify(btController, times(1)).isBtOn()
        verify(btController, times(1)).registerObserver(notificationRepo)
        verify(notificationRepo, times(1)).isPermission()
        verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
        verify(btLogsDao, times(1)).insertReport(logReport.copy(isSuccess = true))
        verify(btDeviceDao, times(1)).getAll()

        verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
        verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
        verifyBlocking(appCache, times(1)) { appCache.loadInCacheSync() }
        verifyBlocking(notificationRepo, times(1)) { notificationRepo.postNotification(null) }
    }

    @Test
    fun `When all permissions are granted and BT is on and device was connected, but it is now connected, then returns success result and does not call notification`() = runTest {

        // ARRANGE
        btController.stub {
            onBlocking { it.getConnectedBleDevices() } doAnswer { setOf() }
            onBlocking { it.getConnectedBtDevices() } doAnswer { setOf(btDevice2) }
        }
        btDeviceDao.stub {
            onBlocking { it.getAll() } doAnswer {listOf(btConnectedDeviceInPast)}
        }
        btLogsDao.stub {
            onBlocking {
                it.insertReport(logReport.copy(isSuccess = true, connectedDevices = listOf(btDevice2)))
            } doAnswer {}
        }

        // ACTION
        val result = sut.onWorkerCall()

        // CHECK
        Assert.assertEquals(ListenableWorker.Result.success(), result)
        verify(btController, times(1)).isPermission()
        verify(btController, times(1)).isBtOn()
        verify(btController, times(1)).registerObserver(notificationRepo)
        verify(notificationRepo, times(1)).isPermission()
        verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
        verify(btLogsDao, times(1)).insertReport(logReport.copy(isSuccess = true, connectedDevices = listOf(btDevice2)))
        verify(btDeviceDao, times(1)).getAll()

        verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
        verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
        verifyBlocking(appCache, times(1)) { appCache.loadInCacheSync() }
        verifyBlocking(notificationRepo, never()) { notificationRepo.postNotification(null) }
    }

    @Test
    fun `When all permissions are granted and BT is on and not tracked device is connected, then returns success result and does call notification`() = runTest {

        // ARRANGE
        btController.stub {
            onBlocking { it.getConnectedBleDevices() } doAnswer { setOf() }
            onBlocking { it.getConnectedBtDevices() } doAnswer { setOf(btDevice1) }
        }
        btDeviceDao.stub {
            onBlocking { it.getAll() } doAnswer {listOf(btConnectedDeviceInPast)}
        }
        btLogsDao.stub {
            onBlocking {
                it.insertReport(logReport.copy(isSuccess = true, connectedDevices = listOf(btDevice1)))
            } doAnswer {}
        }

        // ACTION
        val result = sut.onWorkerCall()

        // CHECK
        Assert.assertEquals(ListenableWorker.Result.success(), result)
        verify(btController, times(1)).isPermission()
        verify(btController, times(1)).isBtOn()
        verify(btController, times(1)).registerObserver(notificationRepo)
        verify(notificationRepo, times(1)).isPermission()
        verify(btLogsDao, times(1)).deleteOlderItemsThan(minusThreeDays)
        verify(btLogsDao, times(1)).insertReport(logReport.copy(isSuccess = true, connectedDevices = listOf(btDevice1)))
        verify(btDeviceDao, times(1)).getAll()

        verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
        verifyBlocking(btController, times(1)) { btController.getConnectedBtDevices() }
        verifyBlocking(appCache, times(1)) { appCache.loadInCacheSync() }
        verifyBlocking(notificationRepo, times(1)) { notificationRepo.postNotification(null) }
    }

}