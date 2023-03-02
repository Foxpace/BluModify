package com.tomasrepcik.blumodify.bluetooth.controllers.blumodify

import android.content.Context
import android.util.Log
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BluetoothControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BluetoothObserver


class BluModifyController(context: Context, private val btController: BluetoothControllerTemplate) :
    BluModifyControllerTemplate(), BluetoothObserver {

    private val tag: String = "BluModifyController"

    override suspend fun initialize() {
        running.value = true
        btController.initialize()
        btController.registerObserver(this)
    }

    override suspend fun dispose() {
        running.value = false
        btController.dispose()
    }

    override fun onBtChange() {
        Log.i(tag, "Bt changed to ${btController.isBtOn()}")
    }
}