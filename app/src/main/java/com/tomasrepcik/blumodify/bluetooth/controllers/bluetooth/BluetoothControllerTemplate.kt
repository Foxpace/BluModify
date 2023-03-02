package com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth


abstract class BluetoothControllerTemplate {

    open fun isBtOn(): Boolean {
        throw NotImplementedError()
    }

    open fun registerObserver(btObserver: BluetoothObserver){
        throw NotImplementedError()
    }

    open fun removeObserver(){
        throw NotImplementedError()
    }

    open fun initialize(){
        throw NotImplementedError()
    }

    open fun dispose() {
        throw NotImplementedError()
    }

}