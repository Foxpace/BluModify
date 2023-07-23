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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(JUnit4::class)
class DeviceListViewModelTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val androidLogs = AndroidLogMockRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var btDeviceDao: BtDeviceDao

    private lateinit var sut: DeviceListViewModel

    @Before
    fun setUp() {
        sut = DeviceListViewModel(btDeviceDao)
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