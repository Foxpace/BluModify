package com.tomasrepcik.blumodify.bluetooth.controllers.blumodify

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BluModifyControllerTemplate {

    protected val running: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRunning = running.asStateFlow()

    abstract suspend fun initialize()

    abstract suspend fun dispose()

}