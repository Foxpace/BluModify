package com.tomasrepcik.blumodify.main.settings.devicelist.viewmodel

import com.tomasrepcik.blumodify.main.settings.shared.model.BtDeviceToPick

sealed class DeviceListState {

    object Loading : DeviceListState()
    object Empty : DeviceListState()
    class Devices(val devices: List<BtDeviceToPick>) : DeviceListState()

}