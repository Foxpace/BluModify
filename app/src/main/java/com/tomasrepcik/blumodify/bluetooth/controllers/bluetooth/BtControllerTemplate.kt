package com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth

import android.bluetooth.BluetoothDevice


interface  BtControllerTemplate {

    fun isBtOn(): Boolean

    fun initialize()

    fun dispose()

    fun registerObserver(btObserver: BtObserver)

    fun removeObserver(btObserver: BtObserver)

    fun getPairedBtDevices(): List<BluetoothDevice>

}