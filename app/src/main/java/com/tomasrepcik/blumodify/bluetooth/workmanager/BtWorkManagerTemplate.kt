package com.tomasrepcik.blumodify.bluetooth.workmanager

import com.tomasrepcik.blumodify.app.model.AppResult

interface BtWorkManagerTemplate {

    suspend fun workersWork(): AppResult<Boolean>
    suspend fun initWorkers()
    suspend fun disposeWorkers()

}