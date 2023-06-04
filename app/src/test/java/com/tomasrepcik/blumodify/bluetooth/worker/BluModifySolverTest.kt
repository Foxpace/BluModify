package com.tomasrepcik.blumodify.bluetooth.worker

import androidx.work.ListenableWorker
import com.tomasrepcik.blumodify.app.notifications.NotificationRepoTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.app.time.TimeRepoTemplate
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class BluModifySolverTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val androidLogs = AndroidLogMockRule()

    @Mock
    private var btController: BtControllerTemplate = mock()

    @Mock
    private var btDeviceDao: BtDeviceDao = mock()

    @Mock
    private var btLogsDao: LogsDao = mock()

    @Mock
    private var appCache: AppCacheTemplate<AppCacheState> = mock()

    @Mock
    private var notificationRepo: NotificationRepoTemplate = mock()

    @Mock
    private var timeRepo: TimeRepoTemplate = mock()


    private var sut: BluModifySolverTemplate =
        BluModifySolver(btController, btDeviceDao, btLogsDao, appCache, notificationRepo, timeRepo)

    private val minusThreeDays = (-3 * 24 * 3600 * 1000).toLong()
    private val logReport = LogReport(
        startTime = 0L,
        connectedDevices = emptyList(),
        isSuccess = false,
        stackTrace = "Missing Bluetooth permission"
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
            on (it.currentMillis()) doAnswer {0L}
        }
        sut = BluModifySolver(btController, btDeviceDao, btLogsDao, appCache, notificationRepo, timeRepo)
    }

    @After
    fun tearDown() = reset(btController, btDeviceDao, btLogsDao, appCache, notificationRepo, timeRepo)

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
        verify(btLogsDao, times(1)).insertReport(logReport)
    }
}