package com.tomasrepcik.blumodify.settings.logs.detail.vm

import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem

sealed class LogsDetailState {
    object Loading: LogsDetailState()
    object NotFound: LogsDetailState()
    class Error(val error: AppResult.Error<ErrorCause>): LogsDetailState()
    class Loaded(val log: LogReportUiItem): LogsDetailState()
}
