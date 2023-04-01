package com.tomasrepcik.blumodify.settings.logs.detail.vm

import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem

sealed class LogDetailState {
    object Loading: LogDetailState()
    object NotFound: LogDetailState()
    class Loaded(val log: LogReportUiItem): LogDetailState()
}
