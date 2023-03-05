package com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth

import android.bluetooth.BluetoothDevice


interface  BluetoothControllerTemplate {

    fun isBtOn(): Boolean

    fun initialize()

    fun dispose()

    fun registerObserver(btObserver: BluetoothObserver)

    fun removeObserver()

    fun getPairedBtDevices(): List<BluetoothDevice>

}