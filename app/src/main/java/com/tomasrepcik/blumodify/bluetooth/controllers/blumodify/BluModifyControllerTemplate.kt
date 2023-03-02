package com.tomasrepcik.blumodify.bluetooth.controllers.blumodify

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BluModifyControllerTemplate {

    protected val running: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRunning = running.asStateFlow()

    open suspend fun initialize(){
        throw NotImplementedError()
    }

    open suspend fun dispose(){
        throw NotImplementedError()
    }

}