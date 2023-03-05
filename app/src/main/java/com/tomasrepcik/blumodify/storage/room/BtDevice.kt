package com.tomasrepcik.blumodify.storage.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BtDevice(
    @PrimaryKey(autoGenerate = false) val macAddress: String,
    @ColumnInfo(name = "deviceName") val name: String,
    @ColumnInfo(name = "wasConnected") val wasConnected: Boolean,
    @ColumnInfo(name = "lastCheck") val lastConnection: Long
)
