package com.tomasrepcik.blumodify.main.settings.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BtControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BtObserver
import com.tomasrepcik.blumodify.main.settings.model.BtDeviceToPick
import com.tomasrepcik.blumodify.main.settings.model.TrackedDevicePickerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BtPickerViewModel @Inject constructor(private val btController: BtControllerTemplate): ViewModel(),
    BtObserver {

    private val tag: String = "BtPickerViewModel"

    private val isShowingBt get() = when(trackedDevicePickerState.value){
        is TrackedDevicePickerState.DevicesToAdd -> true
        TrackedDevicePickerState.Loading -> true
        TrackedDevicePickerState.NoDeviceToAdd -> true
        else -> false
    }

    private val _trackedDevicePickerState: MutableStateFlow<TrackedDevicePickerState> =
        MutableStateFlow(TrackedDevicePickerState.Loading)
    var trackedDevicePickerState = _trackedDevicePickerState.asStateFlow()

    fun onLaunch(context: Context) {
        Log.i(tag, "First launch of the picker")
        _trackedDevicePickerState.value = TrackedDevicePickerState.Loading
        btController.registerObserver(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val isPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission_group.NEARBY_DEVICES
            ) == PackageManager.PERMISSION_GRANTED
            if (!isPermission) {
                Log.w(tag, "Permission for the bluetooth is not available")
                return
            }

        }
        onBtPermissionGranted()
    }

    override fun onBtChange() {
        if (btController.isBtOn() && _trackedDevicePickerState.value is TrackedDevicePickerState.RequireBtOn){
            onBtOn()
        }

        if (!btController.isBtOn() && isShowingBt){
            _trackedDevicePickerState.value = TrackedDevicePickerState.RequireBtOn
        }
    }

    fun onBtPermissionGranted() {
        Log.i(tag, "Permission was granted")
        if (!btController.isBtOn()){
            Log.w(tag, "Bluetooth is not on")
            _trackedDevicePickerState.value = TrackedDevicePickerState.RequireBtOn
            return
        }
        showDevices()
    }

    fun onBtOn(){
        Log.i(tag, "Bluetooth was turned on")
        showDevices()
    }

    @SuppressLint("MissingPermission")
    private fun showDevices() {
        Log.i(tag, "Trying to show the bt devices")
        val devices = btController.getPairedBtDevices()
        if (devices.isEmpty()) {
            Log.w(tag, "No bt devices have been found")
            _trackedDevicePickerState.value = TrackedDevicePickerState.NoDeviceToAdd
            return
        }

        Log.i(tag, "Found ${devices.size} by devices - showing them in the ui")
        val devicesToPick = devices.map { device -> BtDeviceToPick(device.address, device.name) }
        _trackedDevicePickerState.value = TrackedDevicePickerState.DevicesToAdd(devicesToPick)
    }

    fun onDispose() {
        btController.removeObserver(this)
    }
}