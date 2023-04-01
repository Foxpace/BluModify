package com.tomasrepcik.blumodify.settings.logs.list.vm

import com.tomasrepcik.blumodify.settings.logs.list.LogReportUiListItem

sealed class LogsState {
    object Loading: LogsState()
    object NoLogs: LogsState()
    class Logs(val logs: List<LogReportUiListItem>): LogsState()
}
