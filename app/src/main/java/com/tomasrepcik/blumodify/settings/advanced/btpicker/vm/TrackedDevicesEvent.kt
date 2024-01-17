package com.tomasrepcik.blumodify.settings.advanced.btpicker.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

sealed class TrackedDevicesEvent{
    data object OnLaunch : TrackedDevicesEvent()
    data object OnPermissionGranted: TrackedDevicesEvent()
    data object OnBtOn: TrackedDevicesEvent()
    class OnDevicePick(val btItem: BtItem): TrackedDevicesEvent()
    data object OnDispose: TrackedDevicesEvent()
}
