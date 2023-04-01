package com.tomasrepcik.blumodify.settings.advanced.btpicker.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtDeviceToPick

sealed class TrackedDevicesState{
    object Loading: TrackedDevicesState()
    object RequirePermission: TrackedDevicesState()
    object RequireBtOn: TrackedDevicesState()
    object NoDeviceToAdd: TrackedDevicesState()
    object AllDevicesAdded: TrackedDevicesState()
    class DevicesToAdd(val devices: List<BtDeviceToPick>): TrackedDevicesState()
}
