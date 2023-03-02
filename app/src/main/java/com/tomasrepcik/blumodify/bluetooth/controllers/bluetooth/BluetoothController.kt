package com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


class BluetoothController (private val context: Context): BluetoothControllerTemplate() {

    private val tag: String = "BluetoothController"
    private val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.action?.let {
                onBtStateChange(it)
            }
        }
    }
    private var btObserver: BluetoothObserver? = null
    private var lastState: Boolean? = null

    override fun registerObserver(btObserver: BluetoothObserver) {
        registerReceiver()
        this.btObserver = btObserver
    }

    override fun removeObserver() {
        removeReceiver()
        btObserver = null
        lastState = null
        context.applicationContext
    }


    override fun initialize(){

    }

    override fun dispose() {
        removeObserver()
    }

    private fun registerReceiver(){
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)

        context.registerReceiver(receiver, intentFilter)
    }

    private fun removeReceiver() {
        context.unregisterReceiver(receiver)
    }

    override fun isBtOn(): Boolean = bluetoothAdapter.isEnabled

    private fun onBtStateChange(action: String) {
        Log.i(tag, "Received action: $action")
        if (lastState != isBtOn()){
            lastState = isBtOn()
            btObserver?.onBtChange()
        }
    }



}