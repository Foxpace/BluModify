package com.tomasrepcik.blumodify.bluetooth.controllers.blumodify

import android.util.Log
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BtControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BtObserver
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManagerTemplate


class BluModifyController(
    private val btController: BtControllerTemplate,
    private val btWorkManagerTemplate: BtWorkManagerTemplate
) :
    BluModifyControllerTemplate(), BtObserver {

    private val tag: String = "BluModifyController"

    override suspend fun initialize() {
        running.value = true
        btController.initialize()
        btWorkManagerTemplate.initWorkers()
    }

    override suspend fun dispose() {
        running.value = false
    }

    override fun onBtChange() {
        Log.i(tag, "Bt changed to ${btController.isBtOn()}")
    }
}