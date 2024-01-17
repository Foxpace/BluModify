package com.tomasrepcik.blumodify.settings.advanced.devicelist.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

sealed class DeviceListEvent {

    data object OnLaunch : DeviceListEvent()
    class OnDeviceDelete(val btItem: BtItem): DeviceListEvent()

}