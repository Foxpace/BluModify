package com.tomasrepcik.blumodify.settings.advanced.devicelist.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

sealed class DeviceListState {

    data object Loading : DeviceListState()
    data object Empty : DeviceListState()
    data class Devices(val devices: List<BtItem>) : DeviceListState()

}