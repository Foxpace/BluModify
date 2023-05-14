package com.tomasrepcik.blumodify.bluetooth.worker

import androidx.work.ListenableWorker.Result

interface BluModifySolverTemplate {
    suspend fun onWorkerCall(): Result
}