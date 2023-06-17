package com.tomasrepcik.blumodify.bluetooth.workmanager

import com.tomasrepcik.blumodify.app.model.AppResult
import java.util.UUID

interface BtWorkManagerTemplate {

    suspend fun workersWork(): AppResult<Boolean>
    suspend fun initWorkers(): UUID
    suspend fun disposeWorkers()

}