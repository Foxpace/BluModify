package com.tomasrepcik.blumodify.settings.logs.list

data class LogUiListItem(
    val id: Int,
    val time: String,
    val isSuccess: Boolean,
    val connectedDevices: String
)