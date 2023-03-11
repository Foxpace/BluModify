package com.tomasrepcik.blumodify.main.settings.btpicker.model

sealed class TrackedDevicesState{
    object Loading: TrackedDevicesState()
    object RequirePermission: TrackedDevicesState()
    object RequireBtOn: TrackedDevicesState()
    object NoDeviceToAdd: TrackedDevicesState()
    object AllDevicesAdded: TrackedDevicesState()
    class DevicesToAdd(val devices: List<BtDeviceToPick>): TrackedDevicesState()
}
