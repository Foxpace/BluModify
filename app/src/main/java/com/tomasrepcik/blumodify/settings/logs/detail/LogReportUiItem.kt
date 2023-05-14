package com.tomasrepcik.blumodify.settings.logs.detail

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

data class LogReportUiItem(
    val id: String,
    val time: String,
    val isSuccess: Boolean,
    val connectedDevices: Array<BtItem>,
    val stackTrace: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogReportUiItem

        if (id != other.id) return false
        if (time != other.time) return false
        if (isSuccess != other.isSuccess) return false
        if (!connectedDevices.contentEquals(other.connectedDevices)) return false
        if (stackTrace != other.stackTrace) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + isSuccess.hashCode()
        result = 31 * result + connectedDevices.contentHashCode()
        result = 31 * result + stackTrace.hashCode()
        return result
    }
}