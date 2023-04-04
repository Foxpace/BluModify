package com.tomasrepcik.blumodify.bluetooth.worker

import android.content.Context
import androidx.work.ListenableWorker.Result

interface BluModifySolverTemplate {
    suspend fun onWorkerCall(context: Context, isAdvanced: Boolean): Result
}