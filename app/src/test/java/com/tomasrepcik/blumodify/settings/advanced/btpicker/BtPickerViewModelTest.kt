package com.tomasrepcik.blumodify.settings.advanced.btpicker

import app.cash.turbine.turbineScope
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import com.tomasrepcik.blumodify.settings.advanced.btpicker.vm.BtPickerViewModel
import com.tomasrepcik.blumodify.settings.advanced.btpicker.vm.TrackedDevicesEvent
import com.tomasrepcik.blumodify.settings.advanced.btpicker.vm.TrackedDevicesState
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
class BtPickerViewModelTest {

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

    private lateinit var sut: BtPickerViewModel

    @Before
    fun setUp() {
        sut = BtPickerViewModel(btController, btDeviceDao)
    }

    @Test
    fun `Initial state of the BT picker`() {
        assertEquals(sut.trackedDevicesPickerState.value, TrackedDevicesState.Loading)
    }

    @Test
    fun `On Launch of the ViewModel - missing permission`() = runTest {
        turbineScope {
            // ARRANGE
            btController.stub {
                on { this.isPermission() } doAnswer { false }
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.RequirePermission, receiver.awaitItem())
            receiver.cancel()

            verify(btController, times(0)).registerObserver(sut)
        }
    }

    @Test
    fun `On Launch of the ViewModel - on permission granted - no BT`() = runTest {
        turbineScope {
            // ARRANGE
            btController.stub {
                on { isPermission() } doAnswer { false }
                on { registerObserver(sut) } doAnswer {}
                on { isBtOn() } doAnswer { false }
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.RequirePermission, receiver.awaitItem())

            btController.stub {
                on { isPermission() } doAnswer { true }
            }

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnPermissionGranted)

            // CHECK
            assertEquals(TrackedDevicesState.RequireBtOn, receiver.awaitItem())

            receiver.cancel()
            verify(btController, times(1)).registerObserver(sut)
        }
    }

    @Test
    fun `On Launch of the ViewModel - no BT`() = runTest {
        turbineScope {
            // ARRANGE
            btController.stub {
                on { this.isPermission() } doAnswer { true }
                on { registerObserver(sut) } doAnswer {}
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.RequireBtOn, receiver.awaitItem())
            receiver.cancel()

            verify(btController, times(2)).registerObserver(sut)
        }
    }

    @Test
    fun `On Launch of the ViewModel - shows paired devices`() = runTest {
        turbineScope {
            // ARRANGE
            val btDevice = BtItem("BtDevice", "00:00")
            btController.stub {
                on { isPermission() } doAnswer { true }
                on { registerObserver(sut) } doAnswer {}
                on { isBtOn() } doAnswer { true }
                onBlocking { getPairedBtDevices() } doAnswer { setOf(btDevice) }
            }
            btDeviceDao.stub {
                on { getMacAddresses() } doAnswer { listOf() }
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.DevicesToAdd(listOf(btDevice)), receiver.awaitItem())
            receiver.cancel()

            verify(btController, times(2)).registerObserver(sut)
        }
    }

    @Test
    fun `On Launch of the ViewModel - no paired device`() = runTest {
        turbineScope {
            // ARRANGE
            btController.stub {
                on { isPermission() } doAnswer { true }
                on { registerObserver(sut) } doAnswer {}
                on { isBtOn() } doAnswer { true }
                onBlocking { getPairedBtDevices() } doAnswer { setOf() }
            }
            btDeviceDao.stub {
                on { getMacAddresses() } doAnswer { listOf() }
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.NoDeviceToAdd, receiver.awaitItem())
            receiver.cancel()

            verify(btController, times(2)).registerObserver(sut)
        }
    }

    @Test
    fun `On Launch of the ViewModel - the device has been added - all are added`() = runTest {
        turbineScope {
            // ARRANGE
            val btDevice = BtItem("BtDevice", "00:00")
            btController.stub {
                on { isPermission() } doAnswer { true }
                on { registerObserver(sut) } doAnswer {}
                on { isBtOn() } doAnswer { true }
                onBlocking { getPairedBtDevices() } doAnswer { setOf(btDevice) }
            }
            btDeviceDao.stub {
                on { getMacAddresses() } doAnswer { listOf("00:00") }
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.AllDevicesAdded, receiver.awaitItem())
            receiver.cancel()

            verify(btController, times(2)).registerObserver(sut)
        }
    }

    @Test
    fun `On Launch of the ViewModel - 1 device has been added, the other not`() = runTest {
        turbineScope {
            // ARRANGE
            val btDevice = BtItem("BtDevice", "00:00")
            val btDevice2 = BtItem("BtDevice2", "10:00")
            btController.stub {
                on { isPermission() } doAnswer { true }
                on { registerObserver(sut) } doAnswer {}
                on { isBtOn() } doAnswer { true }
                onBlocking { getPairedBtDevices() } doAnswer { setOf(btDevice, btDevice2) }
            }
            btDeviceDao.stub {
                on { getMacAddresses() } doAnswer { listOf("00:00") }
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.DevicesToAdd(listOf(btDevice2)), receiver.awaitItem())
            receiver.cancel()

            verify(btController, times(2)).registerObserver(sut)
        }
    }

    @Test
    fun `On dispose - remove listener`() = runTest {
        turbineScope {
            // ARRANGE
            btController.stub {
                on { isPermission() } doAnswer { true }
                on { registerObserver(sut) } doAnswer {}
                on { isBtOn() } doAnswer { true }
                onBlocking { getPairedBtDevices() } doAnswer { setOf() }
            }
            btDeviceDao.stub {
                on { getMacAddresses() } doAnswer { listOf() }
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.NoDeviceToAdd, receiver.awaitItem())

            // ACTION
            sut.onEvent(TrackedDevicesEvent.OnDispose).join()

            // CHECK
            receiver.cancel()
            verify(btController, times(2)).registerObserver(sut)
            verify(btController, times(1)).removeObserver(sut)
        }
    }

    @Test
    fun `On Launch of the ViewModel - no BT - user turns it on`() = runTest {
        turbineScope {
            // ARRANGE
            val btDevice = BtItem("BtDevice", "00:00")
            btController.stub {
                on { isPermission() } doAnswer { true }
                on { registerObserver(sut) } doAnswer {}
                on { isBtOn() } doAnswer { false }
                onBlocking { getPairedBtDevices() } doAnswer { setOf(btDevice) }
            }
            btDeviceDao.stub {
                on { getMacAddresses() } doAnswer { listOf() }
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION - on launch
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK - on launch
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.RequireBtOn, receiver.awaitItem())

            // ACTION - bt on
            btController.stub {
                on { isBtOn() } doAnswer { true }
            }
            sut.onEvent(TrackedDevicesEvent.OnBtOn)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.DevicesToAdd(listOf(btDevice)), receiver.awaitItem())

            receiver.cancel()

            verify(btController, times(2)).registerObserver(sut)
        }
    }

    @Test
    fun `On Launch of the ViewModel - user picks device to add`() = runTest {
        turbineScope {
            // ARRANGE
            val btItem = BtItem("BtDevice", "00:00")
            val btDevice = BtDevice("00:00", "BtDevice", false, -1L)
            btController.stub {
                on { isPermission() } doAnswer { true }
                on { registerObserver(sut) } doAnswer {}
                on { isBtOn() } doAnswer { true }
                onBlocking { getPairedBtDevices() } doAnswer { setOf(btItem) }
            }
            btDeviceDao.stub {
                on { getMacAddresses() } doAnswer { listOf() }
                on { insertBtDevice(btDevice) } doAnswer {}
            }
            val receiver = sut.trackedDevicesPickerState.testIn(this)

            // ACTION - on launch
            sut.onEvent(TrackedDevicesEvent.OnLaunch)

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.DevicesToAdd(listOf(btItem)), receiver.awaitItem())

            // ACTION - adding item
            btDeviceDao.stub {
                on { getMacAddresses() } doAnswer { listOf("00:00") }
            }
            sut.onEvent(TrackedDevicesEvent.OnDevicePick(btItem))

            // CHECK
            assertEquals(TrackedDevicesState.Loading, receiver.awaitItem())
            assertEquals(TrackedDevicesState.AllDevicesAdded, receiver.awaitItem())

            receiver.cancel()

            verify(btController, times(2)).registerObserver(sut)
            verify(btDeviceDao, times(1)).insertBtDevice(btDevice)
        }
    }

}