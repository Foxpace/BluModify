package com.tomasrepcik.blumodify.bluetooth.controllers.blumodify

import android.util.Log
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BluetoothControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BluetoothObserver
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManagerTemplate


class BluModifyController(
    private val btController: BluetoothControllerTemplate,
    private val btWorkManagerTemplate: BtWorkManagerTemplate
) :
    BluModifyControllerTemplate(), BluetoothObserver {

    private val tag: String = "BluModifyController"

    override suspend fun initialize() {
        running.value = true
        btController.initialize()
        btController.registerObserver(this)
        btWorkManagerTemplate.initWorkers()
    }

    override suspend fun dispose() {
        running.value = false
        btController.dispose()
    }

    override fun onBtChange() {
        Log.i(tag, "Bt changed to ${btController.isBtOn()}")
    }
}