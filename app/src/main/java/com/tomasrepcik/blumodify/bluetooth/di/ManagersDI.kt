package com.tomasrepcik.blumodify.bluetooth.di

import android.content.Context
import androidx.work.WorkManager
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BluetoothController
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BluetoothControllerTemplate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ManagersDI {

    @Provides
    fun providesBluetoothController(@ApplicationContext context: Context): BluetoothControllerTemplate =
        BluetoothController(context)

    @Provides
    fun providesWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)
}