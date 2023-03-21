package com.tomasrepcik.blumodify.settings.devicelist.viewmodel

import com.tomasrepcik.blumodify.settings.shared.model.BtDeviceToPick

sealed class DeviceListState {

    object Loading : DeviceListState()
    object Empty : DeviceListState()
    class Devices(val devices: List<BtDeviceToPick>) : DeviceListState()

}