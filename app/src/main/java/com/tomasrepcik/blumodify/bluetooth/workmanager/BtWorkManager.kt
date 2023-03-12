package com.tomasrepcik.blumodify.bluetooth.workmanager

import android.util.Log
import androidx.work.*
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.bluetooth.workmanager.workers.BtWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class BtWorkManager(private val workManager: WorkManager) : BtWorkManagerTemplate {

    private val workerTag = "BtWorker"
    private val tag = "BtWorkManager"
    override suspend fun workersWork(UUID: UUID): AppResult<Boolean> {

        return try {
            val workerInfo = withContext(Dispatchers.IO) {
                val workers = workManager.getWorkInfoById(UUID)
                return@withContext workers.get()
            }
            val running = withContext(Dispatchers.Default) {
                if (workerInfo == null){
                    return@withContext false
                }
                val state = workerInfo.state
                return@withContext state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED
            }
            AppResult.Success(running)
        } catch (e: ExecutionException) {
            Log.e(tag, e.stackTraceToString())
            AppResult.Error(
                message = "Failed to get state of the Worker",
                errorCause = ErrorCause.WORKER_NOT_FOUND,
                error = "Message: ${e.message}\n\nStackTrace: ${e.stackTraceToString()}"
            )
        } catch (e: InterruptedException) {
            Log.e(tag, e.stackTraceToString())
            AppResult.Error(
                message = "Failed to get state of the Worker",
                errorCause = ErrorCause.WORKER_NOT_FOUND,
                error = "Message: ${e.message}\n\nStackTrace: ${e.stackTraceToString()}"
            )
        }
    }

    override suspend fun initWorkers(oldUUID: UUID, newUUID: UUID) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .setRequiresStorageNotLow(false)
            .setRequiresDeviceIdle(false)
            .setRequiresStorageNotLow(false)
            .build()

        val btWork: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<BtWorker>(
                repeatInterval = 15, TimeUnit.MINUTES,
            ).setConstraints(constraints)
                .addTag(workerTag)
                .setId(newUUID)
                .build()

        disposeWorkers(oldUUID)
        val workerEnqueue = workManager.enqueueUniquePeriodicWork(
            workerTag,
            ExistingPeriodicWorkPolicy.UPDATE,
            btWork
        )
        workerEnqueue.await()
    }

    override suspend fun disposeWorkers(UUID: UUID) {
        val operation = workManager.cancelWorkById(UUID)
        operation.await()
    }
}