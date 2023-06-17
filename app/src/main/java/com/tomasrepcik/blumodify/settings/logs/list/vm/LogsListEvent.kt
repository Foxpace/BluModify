package com.tomasrepcik.blumodify.settings.logs.list.vm

sealed class LogsListEvent {
    object OnLaunch: LogsListEvent()
    object OnReverse: LogsListEvent()
}
