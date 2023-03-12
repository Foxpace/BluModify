package com.tomasrepcik.blumodify.bluetooth.di

import androidx.work.WorkManager
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManager
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManagerTemplate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object BluModifyDI {

    @Provides
    fun providesBtWorker(
        workManager: WorkManager
    ): BtWorkManagerTemplate =
        BtWorkManager(workManager)

}