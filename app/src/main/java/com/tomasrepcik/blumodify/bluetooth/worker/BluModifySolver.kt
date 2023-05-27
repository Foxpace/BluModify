package com.tomasrepcik.blumodify.bluetooth.worker

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.work.ListenableWorker.Result
import com.tomasrepcik.blumodify.app.notifications.model.NotificationAssets
import com.tomasrepcik.blumodify.app.notifications.NotificationRepoTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BluModifySolver @Inject constructor(
    private val btController: BtControllerTemplate,
    private val btDeviceDao: BtDeviceDao,
    private val btLogsDao: LogsDao,
    private val appCache: AppCacheTemplate<AppCacheState>,
    private val notificationRepo: NotificationRepoTemplate
) : BluModifySolverTemplate {

    @SuppressLint("MissingPermission")
    override suspend fun onWorkerCall(assets: NotificationAssets?): Result = withContext(Dispatchers.Main) {
        Log.i(TAG, "BluModify worker was initialized")

        btController.registerObserver(notificationRepo)
        deleteOldLogs()

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
            writeErrorLog(btLogsDao, "Missing notification permission", null)
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
                advancedTracker(assets, connectedBtDevices)
            } catch (e: Exception) {
                Log.e(TAG, "Error occurred during resolving advanced BT connections", e)
                writeErrorLog(btLogsDao, e.stackTraceToString(), connectedBtDevices)
                return@withContext Result.failure()
            }
        } else {
            if (connectedBtDevices.isEmpty()){
                postNotificationToCancelBt(assets)
            }
        }
        writeSuccessLog(btLogsDao, connectedBtDevices)
        return@withContext Result.success()
    }

    private suspend fun advancedTracker(assets: NotificationAssets?, connectedAddresses: Set<BluetoothDevice>): Result = withContext(Dispatchers.Default) {
        val trackedDevices = btDeviceDao.getAll()
        val connectedDevicesInPast = trackedDevices.filter { it.wasConnected }
        for (connectedDeviceInPast in connectedDevicesInPast) {
            if (!connectedAddresses.any { it.address == connectedDeviceInPast.macAddress }) {
                Log.i(TAG, "Showing notification for the device: ${connectedDeviceInPast.name}")
                postNotificationToCancelBt(assets)
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

    private suspend fun postNotificationToCancelBt(assets: NotificationAssets?) =
        withContext(Dispatchers.Main) {
            Log.i(TAG, "Pushing notification for BT cancellation")
            notificationRepo.postNotification(assets)
        }

    private suspend fun deleteOldLogs() = withContext(Dispatchers.Default) {
        Log.i(TAG, "Deleting older logs than 3 days")
        btLogsDao.deleteOlderItemsThan(System.currentTimeMillis() - 3 * 24 * 3600 * 1000)
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




