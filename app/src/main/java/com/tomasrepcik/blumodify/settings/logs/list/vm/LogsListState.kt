package com.tomasrepcik.blumodify.settings.logs.list.vm

import com.tomasrepcik.blumodify.settings.logs.list.LogUiListItem

sealed class LogsListState {
    object Loading: LogsListState()
    object NoLogs: LogsListState()
    data class Logs(val logs: List<LogUiListItem>): LogsListState()
}
