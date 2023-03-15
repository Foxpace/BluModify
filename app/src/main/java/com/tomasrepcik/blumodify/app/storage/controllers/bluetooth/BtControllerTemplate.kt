package com.tomasrepcik.blumodify.app.storage.controllers.bluetooth

import android.bluetooth.BluetoothDevice


interface  BtControllerTemplate {

    fun isBtOn(): Boolean

    fun initialize()

    fun dispose()

    fun registerObserver(btObserver: BtObserver)

    fun removeObserver(btObserver: BtObserver)

    fun getPairedBtDevices(): Set<BluetoothDevice>

    suspend fun getConnectedBtDevices(): Set<BluetoothDevice>
    suspend fun getConnectedBleDevices(): Set<BluetoothDevice>

}