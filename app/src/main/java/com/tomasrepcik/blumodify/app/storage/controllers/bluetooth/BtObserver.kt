package com.tomasrepcik.blumodify.app.storage.controllers.bluetooth

interface BtObserver {

    fun onBtChange(btIsOn: Boolean)

}