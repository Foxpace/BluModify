package com.tomasrepcik.blumodify.settings.logs.detail

import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

data class LogReportUiItem(
    val id: String,
    val time: String,
    val isSuccess: Boolean,
    val connectedDevices: List<BtItem>,
    val stackTrace: String
)