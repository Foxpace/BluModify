package com.tomasrepcik.blumodify.bluetooth.workmanager

import androidx.work.*
import com.tomasrepcik.blumodify.bluetooth.workmanager.workers.BtWorker
import java.util.concurrent.TimeUnit

class BtWorkManager(private val workManager: WorkManager) : BtWorkManagerTemplate {


    override fun initWorkers() {
        workManager.cancelAllWork()
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false).setRequiresBatteryNotLow(false)
            .setRequiresStorageNotLow(false).setRequiresDeviceIdle(false)
            .setRequiresStorageNotLow(false).build()

        val btWork: WorkRequest =
            PeriodicWorkRequestBuilder<BtWorker>(
                repeatInterval = 15, TimeUnit.MINUTES,
            ).setConstraints(constraints)
                .addTag("BtWork")
                .build()
        workManager.enqueue(btWork)

    }
}