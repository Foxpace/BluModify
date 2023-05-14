package com.tomasrepcik.blumodify.bluetooth.worker

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.work.ListenableWorker.Result
import com.tomasrepcik.blumodify.app.notifications.NotificationRepoTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppCache
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
    private val btLogsDao: LogsDao,
    private val appCache: AppCache,
    private val notificationRepo: NotificationRepoTemplate
) : BluModifySolverTemplate {

    @SuppressLint("MissingPermission")
    override suspend fun onWorkerCall(): Result = withContext(Dispatchers.Main) {
        Log.i(TAG, "BluModify worker was initialized")
        if (!btController.isPermission()) {
            Log.e(TAG, "Missing BT permission to execute worker")
            writeErrorLog(btLogsDao, "Missing Bluetooth permission", null)
            return@withContext Result.failure()
        }

        if (!btController.isBtOn()) {
            Log.i(TAG, "Everything went fine with worker - BT is off already")
            writeSuccessLog(btLogsDao, emptySet())
            return@withContext Result.success()
        }

        if (!notificationRepo.isPermission()) {
            Log.e(TAG, "Missing Notification permission to execute worker")
            writeErrorLog(btLogsDao, "Missing Notification permission", null)
            return@withContext Result.failure()
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
            Log.e(TAG, "Failed to obtain connected devices", e)
            writeErrorLog(btLogsDao, e.stackTraceToString(), null)
            return@withContext Result.failure()
        }

        Log.i(TAG, "Found connected devices: ${connectedBtDevices.size}")
        val advanced: Boolean
        try {
            val settings = appCache.loadInCacheSync()
            advanced = settings.isAdvancedSettings
        } catch (e: Exception) {
            Log.w(TAG, "Failed to load in settings of the app", e)
            writeErrorLog(
                btLogsDao,
                "Failed to load in settings of the app\n${e.stackTraceToString()}",
                connectedBtDevices
            )
            return@withContext Result.failure()
        }
        if (advanced) {
            try {
                Log.i(TAG, "Running advanced option for worker")
                advancedTracker(connectedBtDevices)
            } catch (e: Exception) {
                Log.e(TAG, "Error occurred during resolving advanced BT connections", e)
                writeErrorLog(btLogsDao, e.stackTraceToString(), connectedBtDevices)
                return@withContext Result.failure()
            }
        }
        writeSuccessLog(btLogsDao, connectedBtDevices)
        return@withContext Result.success()
    }

    private suspend fun advancedTracker(connectedAddresses: Set<BluetoothDevice>): Result = withContext(Dispatchers.Default) {
        val trackedDevices = btDeviceDao.getAll()
        val connectedDevicesInPast = trackedDevices.filter { it.wasConnected }
        for (connectedDeviceInPast in connectedDevicesInPast) {
            if (!connectedAddresses.any { it.address == connectedDeviceInPast.macAddress }) {
                Log.i(TAG, "Showing notification for the device: ${connectedDeviceInPast.name}")
                notificationRepo.postNotificationToCancelBt()
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
                    wasConnected = true, lastConnection = System.currentTimeMillis()
                )
            )
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
    ) = withContext(Dispatchers.Default) {

        logsDao.insertReport(LogReport(startTime = System.currentTimeMillis(),
            connectedDevices = connectedDevices?.map { BtItem(it) } ?: emptyList(),
            isSuccess = false,
            stackTrace = errorMessage))
    }

    companion object {
        const val TAG = "BluModifyWorkerSolver"
    }
}




