package com.tomasrepcik.blumodify.settings.logs.list.vm

sealed class LogsEvent {
    object OnLaunch: LogsEvent()
    object OnReverse: LogsEvent()
}
