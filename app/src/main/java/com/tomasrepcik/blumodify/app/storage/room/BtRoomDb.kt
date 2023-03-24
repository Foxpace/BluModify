package com.tomasrepcik.blumodify.app.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.dao.RunReportDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.app.storage.room.entities.Converters
import com.tomasrepcik.blumodify.app.storage.room.entities.RunReport

@Database(entities = [BtDevice::class, RunReport::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun btDao(): BtDeviceDao
    abstract fun runReportDao(): RunReportDao
}