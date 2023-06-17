package com.tomasrepcik.blumodify.settings.logs.detail.vm

sealed class LogsDetailEvent {
    data class OnLaunch(val id: Int?, val error: String?): LogsDetailEvent()
}
