package com.tomasrepcik.blumodify.bluetooth.workmanager.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtControllerTemplate
import com.tomasrepcik.blumodify.app.storage.room.BtDeviceDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

// injecting
@HiltWorker
class BtWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val btController: BtControllerTemplate,
    private val btDeviceDao: BtDeviceDao
): Worker(context, workerParams) {

    private val tag = "BtWorker"

    override fun doWork(): Result {
        Log.i(tag, "haha I am here")
        Log.i(tag, "Bluetooth is on: ${btController.isBtOn()}")
        Log.i(tag, "Get paired devices: ${btController.getPairedBtDevices()}")
        Log.i(tag, "Get stored devices: ${btDeviceDao.getAll()}")
        return Result.success()
    }
}