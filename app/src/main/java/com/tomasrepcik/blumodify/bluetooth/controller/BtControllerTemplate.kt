package com.tomasrepcik.blumodify.bluetooth.controller

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem


interface  BtControllerTemplate {

    fun isBtOn(): Boolean

    fun initialize()

    fun dispose()

    fun isPermission(): Boolean

    fun registerObserver(btObserver: BtObserver)

    fun removeObserver(btObserver: BtObserver)

    fun getPairedBtDevices(): Set<BtItem>

    suspend fun getConnectedBtDevices(): Set<BtItem>
    suspend fun getConnectedBleDevices(): Set<BtItem>

}