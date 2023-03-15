package com.tomasrepcik.blumodify.bluetooth.di

import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtController
import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.worker.BluModifySolver
import com.tomasrepcik.blumodify.bluetooth.worker.BluModifySolverTemplate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BluetoothControllerDI {
    @Binds
    @Singleton
    abstract fun providesBluetoothController(btController: BtController): BtControllerTemplate

    @Binds
    @Singleton
    abstract fun providesBluModifySolver(btController: BluModifySolver): BluModifySolverTemplate

}