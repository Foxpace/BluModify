package com.tomasrepcik.blumodify.app.storage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tomasrepcik.blumodify.settings.shared.model.BtItem

@Entity
data class RunReport(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val macAddress: Int,
    @ColumnInfo(name = "start_time") val startTime: Long,
    @ColumnInfo(name = "connected_devices") val connectedDevices: ArrayList<BtItem>,
    @ColumnInfo(name = "result") val isSuccess: Boolean,
    @ColumnInfo(name = "stacktrace") val stackTrace: String
)
