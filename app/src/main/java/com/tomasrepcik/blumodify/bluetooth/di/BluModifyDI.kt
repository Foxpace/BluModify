package com.tomasrepcik.blumodify.bluetooth.di

import android.content.Context
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BluetoothController
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BluetoothControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.controllers.blumodify.BluModifyController
import com.tomasrepcik.blumodify.bluetooth.controllers.blumodify.BluModifyControllerTemplate
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object BluModifyDI {

    @Provides
    fun providesBluModifyController(@ApplicationContext context: Context, btController: BluetoothControllerTemplate): BluModifyControllerTemplate =
        BluModifyController(context, btController)

    @Provides
    fun providesBluetoothController(@ActivityContext context: Context): BluetoothControllerTemplate =
        BluetoothController(context)

}