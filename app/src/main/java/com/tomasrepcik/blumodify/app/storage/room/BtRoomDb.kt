package com.tomasrepcik.blumodify.app.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.app.storage.room.entities.Converters
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport

@Database(entities = [BtDevice::class, LogReport::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun btDao(): BtDeviceDao
    abstract fun runReportDao(): LogsDao
}