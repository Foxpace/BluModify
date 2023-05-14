package com.tomasrepcik.blumodify.settings.advanced.btpicker.vm

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtControllerTemplate
import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtObserver
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BtPickerViewModel @Inject constructor(
    private val btController: BtControllerTemplate,
    private val db: BtDeviceDao
) : ViewModel(),
    BtObserver {

    private val tag: String = "BtPickerViewModel"

    private val isShowingBt
        get() = when (trackedDevicesPickerState.value) {
            is TrackedDevicesState.DevicesToAdd -> true
            TrackedDevicesState.Loading -> true
            TrackedDevicesState.NoDeviceToAdd -> true
            else -> false
        }

    private val _trackedDevicesState: MutableStateFlow<TrackedDevicesState> =
        MutableStateFlow(TrackedDevicesState.Loading)
    var trackedDevicesPickerState = _trackedDevicesState.asStateFlow()

    fun onEvent(event: TrackedDevicesEvent){
        when(event){
            TrackedDevicesEvent.OnBtOn -> onBtOn()
            is TrackedDevicesEvent.OnDevicePick -> onDevicePick(event.btItem)
            TrackedDevicesEvent.OnDispose -> onDispose()
            TrackedDevicesEvent.OnLaunch -> onLaunch()
            TrackedDevicesEvent.OnPermissionGranted -> onBtPermissionGranted()
        }
    }

    private fun onLaunch() {
        Log.i(tag, "First launch of the picker")
        _trackedDevicesState.value = TrackedDevicesState.Loading

        if (!btController.isPermission()){
            _trackedDevicesState.value = TrackedDevicesState.RequirePermission
            Log.i(tag, "No bluetooth permission")
            return
        }

        btController.registerObserver(this)
        onBtPermissionGranted()
    }

    override fun onBtChange(btIsOn: Boolean) {
        if (btController.isBtOn() && _trackedDevicesState.value is TrackedDevicesState.RequireBtOn) {
            onBtOn()
        }

        if (!btController.isBtOn() && isShowingBt) {
            _trackedDevicesState.value = TrackedDevicesState.RequireBtOn
        }
    }

    private fun onBtPermissionGranted() {
        Log.i(tag, "Permission was granted")
        if (!btController.isBtOn()) {
            Log.w(tag, "Bluetooth is not on")
            _trackedDevicesState.value = TrackedDevicesState.RequireBtOn
            return
        }
        viewModelScope.launch(context = Dispatchers.Main) {
            showDevices()
        }
    }

    private fun onBtOn() {
        Log.i(tag, "Bluetooth was turned on")
        viewModelScope.launch(context = Dispatchers.Main) {
            showDevices()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun showDevices() {
        Log.i(tag, "Trying to show the bt devices")
        _trackedDevicesState.value = TrackedDevicesState.Loading

        val devices = btController.getPairedBtDevices()
        val devicesToPick = withContext(Dispatchers.Default){
            Log.i(tag, "Found ${devices.size} devices")
            val macAddresses = db.getMacAddresses()
            return@withContext devices.filter { device -> !macAddresses.contains(device.address) }
                .map { device -> BtItem(device.name, device.address) }.sortedBy { it.deviceName }
        }

        withContext(Dispatchers.Main) {
            if (devices.isEmpty()) {
                Log.w(tag, "No bt devices have been found")
                _trackedDevicesState.value = TrackedDevicesState.NoDeviceToAdd
                return@withContext
            }

            if(devicesToPick.isEmpty()){
                Log.i(tag, "All devices have been added")
                _trackedDevicesState.value = TrackedDevicesState.AllDevicesAdded
                return@withContext
            }

            Log.i(tag, "Found ${devicesToPick.size} by devices - showing them in the ui")
            _trackedDevicesState.value =
                TrackedDevicesState.DevicesToAdd(devicesToPick)
        }


    }

    private fun onDevicePick(pickedDevice: BtItem) {
        val dbDevice = BtDevice(
            macAddress = pickedDevice.macAddress,
            name = pickedDevice.deviceName,
            false,
            -1L
        )
        viewModelScope.launch(context = Dispatchers.Default) {
            db.insertBtDevice(dbDevice)
            withContext(Dispatchers.Main){
                showDevices()
            }
        }
    }

    private fun onDispose() {
        Log.i(tag, "onDispose called")
        btController.removeObserver(this)
    }
}