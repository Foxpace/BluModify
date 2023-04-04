package com.tomasrepcik.blumodify.settings.logs.detail.vm

import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem

sealed class LogDetailState {
    object Loading: LogDetailState()
    object NotFound: LogDetailState()
    class Error(val error: String): LogDetailState()
    class Loaded(val log: LogReportUiItem): LogDetailState()
}
