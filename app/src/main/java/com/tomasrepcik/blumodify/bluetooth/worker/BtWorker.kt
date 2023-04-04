package com.tomasrepcik.blumodify.bluetooth.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

// injecting
@HiltWorker
class BtWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParams: WorkerParameters,
    private val solver: BluModifySolverTemplate
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = solver.onWorkerCall(
        applicationContext, workerParams.inputData.getBoolean(
            KEY_ADVANCED_SETTINGS, false
        )
    )

    companion object {
        const val KEY_ADVANCED_SETTINGS = "KEY_ADVANCED_SETTINGS"
    }
}