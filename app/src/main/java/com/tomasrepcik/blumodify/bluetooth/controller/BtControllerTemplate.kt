package com.tomasrepcik.blumodify.bluetooth.controller

import android.bluetooth.BluetoothDevice


interface  BtControllerTemplate {

    fun isBtOn(): Boolean

    fun initialize()

    fun dispose()

    fun isPermission(): Boolean

    fun registerObserver(btObserver: BtObserver)

    fun removeObserver(btObserver: BtObserver)

    fun getPairedBtDevices(): Set<BluetoothDevice>

    suspend fun getConnectedBtDevices(): Set<BluetoothDevice>
    suspend fun getConnectedBleDevices(): Set<BluetoothDevice>

}