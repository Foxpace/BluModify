package com.tomasrepcik.blumodify.bluetooth.di

import android.content.Context
import androidx.work.WorkManager
import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtController
import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtControllerTemplate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ManagersDI {

    @Provides
    fun providesBluetoothController(@ApplicationContext context: Context): BtControllerTemplate =
        BtController(context)

    @Provides
    fun providesWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}