package com.tomasrepcik.blumodify.settings.logs.detail

import app.cash.turbine.turbineScope
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import com.tomasrepcik.blumodify.settings.logs.detail.vm.LogsDetailEvent
import com.tomasrepcik.blumodify.settings.logs.detail.vm.LogsDetailState
import com.tomasrepcik.blumodify.settings.logs.detail.vm.LogsDetailViewModel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.stub

class LogDetailViewModelTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val androidLogs = AndroidLogMockRule()

    @Mock
    var logsDao: LogsDao = mock()

    private var sut: LogsDetailViewModel = LogsDetailViewModel(logsDao)

    @Before
    fun setUp() {
        sut = LogsDetailViewModel(logsDao)
    }

    @After
    fun tearDown() {
        reset(logsDao)
    }

    @Test
    fun `Initial state`() {
        assertEquals(LogsDetailState.Loading, sut.logsState.value)
    }

    @Test
    fun `On Launch - no id and no error`() = runTest {
        turbineScope {
            // ARRANGE
            val receiver = sut.logsState.testIn(this)

            // ACTION
            sut.onEvent(LogsDetailEvent.OnLaunch(null, null))

            // CHECK
            assertEquals(LogsDetailState.Loading, receiver.awaitItem())
            assertEquals(LogsDetailState.NotFound, receiver.awaitItem())
            receiver.cancel()
        }
    }

    @Test
    fun `On Launch - no id , but error occurred`() = runTest {
        turbineScope {
            // ARRANGE
            val receiver = sut.logsState.testIn(this)

            // ACTION
            sut.onEvent(LogsDetailEvent.OnLaunch(null, "Error"))

            // CHECK
            assertEquals(LogsDetailState.Loading, receiver.awaitItem())
            assertEquals(
                LogsDetailState.Error(
                    AppResult.Error(
                        message = "Error occurred during searching for correct ID",
                        origin = "LogsScreenDetailViewModel.findLogById",
                        errorCause = ErrorCause.MISSING_ID,
                        stacktrace = "Error"
                    )
                ), receiver.awaitItem()
            )
            receiver.cancel()
        }
    }


    @Test
    fun `On Launch - correct id`() = runTest {
        turbineScope {
            // ARRANGE
            val id = 1
            val receiver = sut.logsState.testIn(this)
            logsDao.stub {
                on { getLogById(id) } doAnswer {
                    LogReport(
                        id = 1,
                        startTime = 0L,
                        connectedDevices = listOf(),
                        isSuccess = true,
                        stackTrace = null
                    )
                }
            }

            // ACTION
            sut.onEvent(LogsDetailEvent.OnLaunch(1, null))

            // CHECK
            assertEquals(LogsDetailState.Loading, receiver.awaitItem())
            assertEquals(
                LogsDetailState.Loaded(
                    LogReportUiItem(
                        "1",
                        time = "1. 1. 1970 1:00",
                        true,
                        connectedDevices = arrayOf(),
                        stackTrace = ""
                    )
                ), receiver.awaitItem()
            )
            receiver.cancel()
        }
    }

    @Test
    fun `On Launch - id not found`() = runTest {
        turbineScope {
            // ARRANGE
            val id = 1
            val receiver = sut.logsState.testIn(this)
            logsDao.stub {
                on { getLogById(id) } doAnswer { null }
            }

            // ACTION
            sut.onEvent(LogsDetailEvent.OnLaunch(1, null))

            // CHECK
            assertEquals(LogsDetailState.Loading, receiver.awaitItem())
            assertEquals(LogsDetailState.NotFound, receiver.awaitItem())
            receiver.cancel()
        }
    }

}