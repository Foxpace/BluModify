package com.tomasrepcik.blumodify.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BtDevice::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun btDao(): BtDeviceDao
}