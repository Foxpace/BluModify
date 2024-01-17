package com.tomasrepcik.blumodify.settings.logs.detail.vm

import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem

sealed class LogsDetailState {
    data object Loading: LogsDetailState()
    data object NotFound: LogsDetailState()
    data class Error(val error: AppResult.Error<ErrorCause>): LogsDetailState()
    data class Loaded(val log: LogReportUiItem): LogsDetailState()
}
