package com.tomasrepcik.blumodify.bluetooth.workmanager

import com.tomasrepcik.blumodify.app.model.AppResult
import java.util.*

interface BtWorkManagerTemplate {

    suspend fun workersWork(UUID: UUID): AppResult<Boolean>
    suspend fun initWorkers(oldUUID: UUID, newUUID: UUID)
    suspend fun disposeWorkers(UUID: UUID)

}