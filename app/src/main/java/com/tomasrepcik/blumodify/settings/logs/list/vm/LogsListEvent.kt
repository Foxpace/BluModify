package com.tomasrepcik.blumodify.settings.logs.list.vm

sealed class LogsListEvent {
    data object OnLaunch: LogsListEvent()
    data object OnReverse: LogsListEvent()
}
