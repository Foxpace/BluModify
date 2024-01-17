package com.tomasrepcik.blumodify.settings.advanced.btpicker.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

sealed class TrackedDevicesState{
    data object Loading: TrackedDevicesState()
    data object RequirePermission: TrackedDevicesState()
    data object RequireBtOn: TrackedDevicesState()
    data object NoDeviceToAdd: TrackedDevicesState()
    data object AllDevicesAdded: TrackedDevicesState()
    data class DevicesToAdd(val devices: List<BtItem>): TrackedDevicesState()
}
