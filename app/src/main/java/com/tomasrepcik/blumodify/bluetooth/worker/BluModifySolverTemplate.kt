package com.tomasrepcik.blumodify.bluetooth.worker

import androidx.work.ListenableWorker.Result
import com.tomasrepcik.blumodify.app.notifications.model.NotificationAssets

interface BluModifySolverTemplate {
    suspend fun onWorkerCall(assets: NotificationAssets?): Result
}