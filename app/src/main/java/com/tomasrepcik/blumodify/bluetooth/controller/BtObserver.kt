package com.tomasrepcik.blumodify.bluetooth.controller

interface BtObserver {

    suspend fun onBtChange(btIsOn: Boolean)

}