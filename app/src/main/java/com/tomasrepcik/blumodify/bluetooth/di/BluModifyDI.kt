package com.tomasrepcik.blumodify.bluetooth.di

import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManager
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManagerTemplate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class BluModifyDI {
    @Binds
    abstract fun providesBtWorker(btManager: BtWorkManager): BtWorkManagerTemplate
}