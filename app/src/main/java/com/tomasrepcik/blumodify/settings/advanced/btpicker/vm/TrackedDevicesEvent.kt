package com.tomasrepcik.blumodify.settings.advanced.btpicker.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

sealed class TrackedDevicesEvent{
    object OnLaunch : TrackedDevicesEvent()
    object OnPermissionGranted: TrackedDevicesEvent()
    object OnBtOn: TrackedDevicesEvent()
    class OnDevicePick(val btItem: BtItem): TrackedDevicesEvent()
    object OnDispose: TrackedDevicesEvent()
}
