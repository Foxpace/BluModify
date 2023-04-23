package com.tomasrepcik.blumodify.bluetooth.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ListenableWorker.Result
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtControllerTemplate
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BluModifySolver @Inject constructor(
    private val btController: BtControllerTemplate,
    private val btDeviceDao: BtDeviceDao,
    private val btLogsDao: LogsDao
) : BluModifySolverTemplate {

    @SuppressLint("MissingPermission")
    override suspend fun onWorkerCall(context: Context, isAdvanced: Boolean): Result =
        withContext(Dispatchers.Main) {
            if (!btController.isBtOn()) {
                writeSuccessLog(btLogsDao, emptySet())
                return@withContext Result.success()
            }

            val connectedBtDevices: Set<BluetoothDevice>

            try {
                connectedBtDevices = withContext(Dispatchers.Default) findDevices@{
                    val allConnectedDevices = mutableSetOf<BluetoothDevice>()
                    allConnectedDevices.addAll(btController.getConnectedBleDevices())
                    allConnectedDevices.addAll(btController.getConnectedBtDevices())
                    return@findDevices allConnectedDevices
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Result.failure()
            }
            Log.i(TAG, "Found connected devices: ${connectedBtDevices.size}")
            if (isAdvanced) {
                try {
                    advancedTracker(context, connectedBtDevices)
                } catch (e: Exception) {
                    Log.e(TAG, "Error occurred during resolving advanced BT connections", e)
                    writeErrorLog(btLogsDao, e.stackTraceToString(), connectedBtDevices)
                    return@withContext Result.failure()
                }
            }
            writeSuccessLog(btLogsDao, connectedBtDevices)
            return@withContext Result.success()
        }

    private suspend fun advancedTracker(
        context: Context,
        connectedAddresses: Set<BluetoothDevice>
    ): Result = withContext(Dispatchers.Default) {
        val trackedDevices = btDeviceDao.getAll()
        val connectedDevicesInPast = trackedDevices.filter { it.wasConnected }
        for (connectedDeviceInPast in connectedDevicesInPast) {
            if (!connectedAddresses.any { it.address == connectedDeviceInPast.macAddress }) {
                Log.i(TAG, "Showing notification for the device: ${connectedDeviceInPast.name}")
                showNotificationToTurnOffBt(context, connectedDeviceInPast)
                break
            }
        }

        if (connectedAddresses.isEmpty()) {
            return@withContext Result.success()
        }

        for (trackedDevice in trackedDevices) {
            val isDeviceConnectedNow =
                connectedAddresses.any { it.address == trackedDevice.macAddress }
            val wasNotConnectedBefore = !trackedDevice.wasConnected
            if (wasNotConnectedBefore && isDeviceConnectedNow) {
                Log.i(TAG, "Starting to track and storing to db: ${trackedDevice.name}")
                insertTrackedDeviceForFutureCheck(trackedDevice)
            }
        }

        return@withContext Result.success()
    }

    private suspend fun insertTrackedDeviceForFutureCheck(trackedDevice: BtDevice) =
        withContext(Dispatchers.Default) {
            btDeviceDao.insertBtDevice(
                trackedDevice.copy(
                    wasConnected = true,
                    lastConnection = System.currentTimeMillis()
                )
            )
        }

    @SuppressLint("MissingPermission")
    private suspend fun showNotificationToTurnOffBt(
        context: Context,
        disconnectedBtDevice: BtDevice
    ) =
        withContext(Dispatchers.Main) {

            createNotificationChannel(context)
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_square)
                .setContentTitle("BluModify")
                .setContentText("Do you want to turn off Bluetooth?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }


    private fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            importance
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private suspend fun writeSuccessLog(logsDao: LogsDao, connectedDevices: Set<BluetoothDevice>) =
        withContext(Dispatchers.Default) {

            logsDao.insertReport(
                LogReport(
                    startTime = System.currentTimeMillis(),
                    connectedDevices = connectedDevices.map { BtItem(it) },
                    isSuccess = true
                )
            )
        }

    private suspend fun writeErrorLog(
        logsDao: LogsDao,
        errorMessage: String,
        connectedDevices: Set<BluetoothDevice>?,
    ) =
        withContext(Dispatchers.Default) {

            logsDao.insertReport(
                LogReport(
                    startTime = System.currentTimeMillis(),
                    connectedDevices = connectedDevices?.map { BtItem(it) } ?: emptyList(),
                    isSuccess = false,
                    stackTrace = errorMessage
                )
            )
        }

    companion object {
        const val TAG = "BluModifyWorkerSolver"
        const val CHANNEL_ID = "com.tomasrepcik.blumodify.notification.channel"
        const val NOTIFICATION_ID = 64684648
    }
}



