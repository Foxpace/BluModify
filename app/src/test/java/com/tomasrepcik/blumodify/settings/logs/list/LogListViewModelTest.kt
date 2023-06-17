package com.tomasrepcik.blumodify.settings.logs.list

import app.cash.turbine.testIn
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import com.tomasrepcik.blumodify.settings.logs.list.vm.LogsListEvent
import com.tomasrepcik.blumodify.settings.logs.list.vm.LogsListState
import com.tomasrepcik.blumodify.settings.logs.list.vm.LogsListViewModel
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

class LogListViewModelTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val androidLogs = AndroidLogMockRule()

    @Mock
    var logsDao: LogsDao = mock()

    private var sut: LogsListViewModel = LogsListViewModel(logsDao)

    @Before
    fun setUp() {
        sut = LogsListViewModel(logsDao)
    }

    @After
    fun tearDown() {
        reset(logsDao)
    }

    @Test
    fun `Initial state`() {
        assertEquals(LogsListState.Loading, sut.logsState.value)
    }

    @Test
    fun `On launch - no logs available in db`() = runTest {
        // ARRANGE
        logsDao.stub {
            on { getAll() } doAnswer { listOf() }
        }
        val receiver = sut.logsState.testIn(this)

        // ACTION
        sut.onEvent(LogsListEvent.OnLaunch)

        // CHECK
        assertEquals(LogsListState.Loading, receiver.awaitItem())
        assertEquals(LogsListState.NoLogs, receiver.awaitItem())

        receiver.cancel()

    }

    @Test
    fun `On launch - 1 item`() = runTest {
        // ARRANGE
        logsDao.stub {
            on { getAll() } doAnswer {
                listOf(
                    LogReport(
                        startTime = 0L,
                        connectedDevices = listOf(),
                        isSuccess = true,
                        stackTrace = null
                    )
                )
            }
        }
        val receiver = sut.logsState.testIn(this)

        // ACTION
        sut.onEvent(LogsListEvent.OnLaunch)

        // CHECK
        assertEquals(LogsListState.Loading, receiver.awaitItem())
        assertEquals(
            LogsListState.Logs(
                listOf(
                    LogUiListItem(
                        id = 0,
                        time = "1. 1. 1970 1:00",
                        isSuccess = true,
                        connectedDevices = "0"
                    )
                )
            ), receiver.awaitItem()
        )

        receiver.cancel()

    }

    @Test
    fun `Reversing items`() = runTest {
        // ARRANGE
        logsDao.stub {
            on { getAll() } doAnswer {
                listOf(
                    LogReport(
                        startTime = 0L,
                        connectedDevices = listOf(),
                        isSuccess = true,
                        stackTrace = null
                    ),
                    LogReport(
                        startTime = 0L,
                        connectedDevices = listOf(),
                        isSuccess = false,
                        stackTrace = null
                    )
                )
            }
        }
        val receiver = sut.logsState.testIn(this)

        // ACTION - on launch
        sut.onEvent(LogsListEvent.OnLaunch)

        // CHECK - on launch
        assertEquals(LogsListState.Loading, receiver.awaitItem())
        assertEquals(
            LogsListState.Logs(
                listOf(
                    LogUiListItem(
                        id = 0,
                        time = "1. 1. 1970 1:00",
                        isSuccess = true,
                        connectedDevices = "0"
                    ),
                    LogUiListItem(
                        id = 0,
                        time = "1. 1. 1970 1:00",
                        isSuccess = false,
                        connectedDevices = "0"
                    )
                )
            ), receiver.awaitItem()
        )

        // ACTION
        sut.onEvent(LogsListEvent.OnReverse)

        // CHECK
        assertEquals(
            LogsListState.Logs(
                listOf(
                    LogUiListItem(
                        id = 0,
                        time = "1. 1. 1970 1:00",
                        isSuccess = false,
                        connectedDevices = "0"
                    ),
                    LogUiListItem(
                        id = 0,
                        time = "1. 1. 1970 1:00",
                        isSuccess = true,
                        connectedDevices = "0"
                    ),
                )
            ), receiver.awaitItem()
        )

        receiver.cancel()

    }
}