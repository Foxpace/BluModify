package com.tomasrepcik.blumodify.app.storage.controllers.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


@SuppressLint("MissingPermission")
class BtController(private val context: Context) : BtControllerTemplate {

    private val tag: String = "BluetoothController"

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter

    private var isReceiverRegistered: Boolean = false
    private val observers: ArrayList<BtObserver> = arrayListOf()

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            intent.action?.let {
                onBtStateChange(it)
            }
        }
    }
    private var lastState: Boolean? = null

    override fun registerObserver(btObserver: BtObserver) {
        if (!isReceiverRegistered){
            registerReceiver()
        }

        if (!observers.contains(btObserver)){
            observers.add(btObserver)
        }
    }

    override fun removeObserver(btObserver: BtObserver) {
        if (observers.contains(btObserver)){
            observers.remove(btObserver)
        }

        if (isReceiverRegistered && observers.isEmpty()){
            removeReceiver()
        }

    }


    override fun initialize() {
        observers.clear()
    }

    override fun dispose() {
        observers.clear()
        if (isReceiverRegistered){
            removeReceiver()
        }
    }


    override fun getPairedBtDevices(): List<BluetoothDevice> =
        bluetoothAdapter.bondedDevices.filter { it.bondState == BluetoothDevice.BOND_BONDED }

    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        context.registerReceiver(receiver, intentFilter)
        isReceiverRegistered = true
    }

    private fun removeReceiver() {
        context.unregisterReceiver(receiver)
        isReceiverRegistered = false
    }

    override fun isBtOn(): Boolean = bluetoothAdapter.isEnabled

    private fun onBtStateChange(action: String) {
        Log.i(tag, "Received action: $action")
        if (lastState != isBtOn()) {
            lastState = isBtOn()
            observers.forEach { it.onBtChange() }
        }
    }


}