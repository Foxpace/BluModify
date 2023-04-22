package com.tomasrepcik.blumodify.settings.advanced.shared.model

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import kotlinx.serialization.Serializable

@Serializable
data class BtItem (val deviceName: String, val macAddress: String){
    @SuppressLint("MissingPermission")
    constructor(btDevice: BluetoothDevice): this(btDevice.name, btDevice.address)
}
