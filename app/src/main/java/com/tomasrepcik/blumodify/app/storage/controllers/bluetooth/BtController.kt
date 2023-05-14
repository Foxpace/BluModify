package com.tomasrepcik.blumodify.app.storage.controllers.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import javax.inject.Inject


@SuppressLint("MissingPermission")
class BtController @Inject constructor(@ApplicationContext private val context: Context) :
    BtControllerTemplate {


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
        Log.i(TAG, "Registering observer")
        if (!isReceiverRegistered) {
            Log.i(TAG, "Registering new receiver for bt events")
            registerReceiver()
        }

        if (!observers.contains(btObserver)) {
            observers.add(btObserver)
        }
    }

    override fun removeObserver(btObserver: BtObserver) {
        if (observers.contains(btObserver)) {
            Log.i(TAG, "Removing BT observer")
            observers.remove(btObserver)
        }

        if (isReceiverRegistered && observers.isEmpty()) {
            Log.i(TAG, "Removing BT reveiver")
            removeReceiver()
        }

    }


    override fun initialize() {
        Log.i(TAG, "Initializing the BT controller")
        observers.clear()
    }

    override fun dispose() {
        Log.i(TAG, "Disposing the BT controller")
        observers.clear()
        if (isReceiverRegistered) {
            removeReceiver()
        }
    }

    override fun isPermission(): Boolean {
        Log.i(TAG, "Checking bluetooth permission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission_group.NEARBY_DEVICES
            ) == PackageManager.PERMISSION_GRANTED
        }
        Log.i(TAG, "Bluetooth permission is not needed - lower than Android S")
        return true
    }


    override fun getPairedBtDevices(): Set<BluetoothDevice> =
        bluetoothAdapter.bondedDevices.filter {
            it.bondState == BluetoothDevice.BOND_BONDED
                    && it.type != BluetoothDevice.DEVICE_TYPE_LE
        }
            .toSet()

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
        Log.i(TAG, "Received action: $action")
        if (lastState != isBtOn()) {
            lastState = isBtOn()
            observers.forEach { it.onBtChange(isBtOn()) }
        }
    }

    // 	int: The Bluetooth profile; either
    // 	BluetoothProfile.HEADSET,
    // 	BluetoothProfile.A2DP,
    // 	BluetoothProfile.GATT,
    // 	BluetoothProfile.HEARING_AID or
    // 	BluetoothProfile.GATT_SERVER.

    override suspend fun getConnectedBtDevices(): Set<BluetoothDevice> =
        withContext(Dispatchers.Default) {
            val devices = mutableSetOf<BluetoothDevice>()
            for (type in btProfile) {
                Log.i(TAG, "Searching BT devices for ${btTypeToString(type)}")
                val connectedDevices = getConnectedDevicesByCallback(type).toList()
                devices.addAll(connectedDevices)
            }
            Log.i(TAG, "Returning ${devices.size} BT devices")
            return@withContext devices
        }

    private fun getConnectedDevicesByCallback(btProfile: Int) = callbackFlow {
        val serviceListener = object : BluetoothProfile.ServiceListener {

            override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
                for (device in proxy.connectedDevices) {
                    val connected =
                        proxy.getConnectionState(device) == BluetoothProfile.STATE_CONNECTED
                    if (connected) {
                        trySend(device)
                            .onClosed { throwable -> throwable?.printStackTrace() }
                            .isSuccess
                    }
                }
                bluetoothAdapter.closeProfileProxy(profile, proxy)
                close()
            }

            override fun onServiceDisconnected(profile: Int) {
                close()
            }
        }

        Log.i(TAG, "Registering our service listener to receive headset connection updates")
        bluetoothAdapter.getProfileProxy(
            context,
            serviceListener,
            btProfile
        )
        awaitClose { channel.close() }
    }

    override suspend fun getConnectedBleDevices(): Set<BluetoothDevice> =
        withContext(Dispatchers.Default) {
            val devices = mutableSetOf<BluetoothDevice>()
            for (profile in bleProfiles) {
                val connectedDevices = bluetoothManager.getConnectedDevices(profile)
                devices.addAll(connectedDevices)
            }
            Log.i(TAG, "Returning ${devices.size} BLE devices")
            return@withContext devices
        }

    companion object {

        const val TAG = "BtController"

        @JvmStatic
        val btProfile = arrayOf(
            BluetoothProfile.HEADSET,
            BluetoothProfile.A2DP,
        )

        @JvmStatic
        val bleProfiles = arrayOf(
            BluetoothProfile.GATT,
            BluetoothProfile.GATT_SERVER
        )

        fun btTypeToString(btType: Int): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                when (btType) {
                    BluetoothProfile.HEADSET -> return "HEADSET"
                    BluetoothProfile.A2DP -> return "A2DP"
                    BluetoothProfile.GATT -> return "GATT"
                    BluetoothProfile.GATT_SERVER -> return "GATT_SERVER"
                    BluetoothProfile.HEARING_AID -> return "HEARING_AID"
                }
            }

            return when (btType) {
                BluetoothProfile.HEADSET -> "HEADSET"
                BluetoothProfile.A2DP -> "A2DP"
                BluetoothProfile.GATT -> "GATT"
                BluetoothProfile.GATT_SERVER -> "GATT_SERVER"
                else -> "UNKNOWN"
            }

        }
    }
}