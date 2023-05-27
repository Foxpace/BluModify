package com.tomasrepcik.blumodify.bluetooth.worker

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tomasrepcik.blumodify.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

// injecting
@HiltWorker
class BtWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted val workerParams: WorkerParameters,
    private val solver: BluModifySolverTemplate
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = solver.onWorkerCall(
        WorkerAssets(
            notificationTitle = R.string.app_name,
            notificationContent = R.string.notification_content,
            intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS),
            notificationButtonIcon = R.drawable.ic_bt_black,
            notificationButtonText = R.string.main_screen_turn_off,
        )
    )
}