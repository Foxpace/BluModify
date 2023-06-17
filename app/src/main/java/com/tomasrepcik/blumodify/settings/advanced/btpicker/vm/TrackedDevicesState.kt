package com.tomasrepcik.blumodify.settings.advanced.btpicker.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

sealed class TrackedDevicesState{
    object Loading: TrackedDevicesState()
    object RequirePermission: TrackedDevicesState()
    object RequireBtOn: TrackedDevicesState()
    object NoDeviceToAdd: TrackedDevicesState()
    object AllDevicesAdded: TrackedDevicesState()
    data class DevicesToAdd(val devices: List<BtItem>): TrackedDevicesState()
}
