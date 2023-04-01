package com.tomasrepcik.blumodify.settings.advanced.devicelist.vm

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtDeviceToPick

sealed class DeviceListState {

    object Loading : DeviceListState()
    object Empty : DeviceListState()
    class Devices(val devices: List<BtDeviceToPick>) : DeviceListState()

}