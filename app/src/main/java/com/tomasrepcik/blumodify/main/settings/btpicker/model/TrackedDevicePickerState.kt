package com.tomasrepcik.blumodify.main.settings.btpicker.model

sealed class TrackedDevicePickerState{
    object Loading: TrackedDevicePickerState()
    object RequirePermission: TrackedDevicePickerState()
    object RequireBtOn: TrackedDevicePickerState()
    object NoDeviceToAdd: TrackedDevicePickerState()
    class DevicesToAdd(val devices: List<BtDeviceToPick>): TrackedDevicePickerState()
}
