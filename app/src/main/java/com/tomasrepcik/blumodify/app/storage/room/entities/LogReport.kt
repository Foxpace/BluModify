package com.tomasrepcik.blumodify.app.storage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

@Entity
data class LogReport(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "start_time") val startTime: Long,
    @ColumnInfo(name = "connected_devices") val connectedDevices: List<BtItem>,
    @ColumnInfo(name = "result") val isSuccess: Boolean,
    @ColumnInfo(name = "stacktrace") val stackTrace: String = ""
)
