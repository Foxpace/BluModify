package com.tomasrepcik.blumodify.settings.advanced.devicelist

import app.cash.turbine.turbineScope
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListEvent
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListState
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListViewModel
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class DeviceListViewModelTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val androidLogs = AndroidLogMockRule()

    @Mock
    var btDeviceDao: BtDeviceDao = mock()

    private var sut: DeviceListViewModel = DeviceListViewModel(btDeviceDao)

    @Before
    fun setUp() {
        sut = DeviceListViewModel(btDeviceDao)
    }

    @After
    fun tearDown() {
        reset(btDeviceDao)
    }

    @Test
    fun `Initial state`() {
        assertEquals(DeviceListState.Loading, sut.listState.value)
    }

    @Test
    fun `On launch - no device stored`() = runTest {
        turbineScope {
            // ARRANGE
            btDeviceDao.stub {
                on { getAll() } doAnswer { listOf() }
            }
            val receiver = sut.listState.testIn(this)

            // ACTION
            sut.onEvent(DeviceListEvent.OnLaunch)

            // CHECK
            assertEquals(DeviceListState.Loading, receiver.awaitItem())
            assertEquals(DeviceListState.Empty, receiver.awaitItem())
            receiver.cancel()
        }
    }

    @Test
    fun `On launch - 1 device stored`() = runTest {
        turbineScope {
            // ARRANGE
            btDeviceDao.stub {
                on { getAll() } doAnswer { listOf(BtDevice("00:00", "BtDevice", false, -1L)) }
            }
            val receiver = sut.listState.testIn(this)

            // ACTION
            sut.onEvent(DeviceListEvent.OnLaunch)

            // CHECK
            assertEquals(DeviceListState.Loading, receiver.awaitItem())
            assertEquals(
                DeviceListState.Devices(listOf(BtItem("BtDevice", "00:00"))), receiver.awaitItem()
            )
            receiver.cancel()
        }
    }

    @Test
    fun `On launch - remove device`() = runTest {
        turbineScope {
            // ARRANGE
            val btItem = BtItem("BtDevice", "00:00")
            val btDevice = BtDevice("00:00", "BtDevice", false, -1L)
            btDeviceDao.stub {
                on { getAll() } doAnswer { listOf(btDevice) }
                on { deleteByMacAddress("00:00") } doAnswer {}
            }
            val receiver = sut.listState.testIn(this)

            // ACTION
            sut.onEvent(DeviceListEvent.OnLaunch)

            // CHECK
            assertEquals(DeviceListState.Loading, receiver.awaitItem())
            assertEquals(
                DeviceListState.Devices(listOf(btItem)), receiver.awaitItem()
            )

            // ACTION - removal of device
            btDeviceDao.stub {
                on { getAll() } doAnswer { listOf() }
            }
            sut.onEvent(DeviceListEvent.OnDeviceDelete(btItem))

            // CHECK
            assertEquals(
                DeviceListState.Loading, receiver.awaitItem()
            )
            assertEquals(
                DeviceListState.Empty, receiver.awaitItem()
            )
            verify(btDeviceDao, times(1)).deleteByMacAddress(btItem.macAddress)

            receiver.cancel()
        }
    }
}