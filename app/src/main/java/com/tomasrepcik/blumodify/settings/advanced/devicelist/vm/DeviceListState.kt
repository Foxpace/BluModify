package com.tomasrepcik.blumodify.settings.advanced.devicelist.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

sealed class DeviceListState {

    object Loading : DeviceListState()
    object Empty : DeviceListState()
    data class Devices(val devices: List<BtItem>) : DeviceListState()

}