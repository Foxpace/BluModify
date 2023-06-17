package com.tomasrepcik.blumodify.settings.logs.detail.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import javax.inject.Inject

@HiltViewModel
class LogsDetailViewModel @Inject constructor(private val logsDao: LogsDao) : ViewModel() {

    private val _logsState: MutableStateFlow<LogsDetailState> =
        MutableStateFlow(LogsDetailState.Loading)
    var logsState = _logsState.asStateFlow()

    fun onEvent(event: LogsDetailEvent): Job = when (event) {
        is LogsDetailEvent.OnLaunch -> findLogById(event.id, event.error)
    }

    private fun findLogById(id: Int?, error: String?): Job = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Searching for log with ID $id")

        if (error != null) {
            Log.e(TAG, "Error occurred during searching for correct ID")
            _logsState.value = LogsDetailState.Error(
                AppResult.Error(
                    message = "Error occurred during searching for correct ID",
                    errorCause = ErrorCause.MISSING_ID,
                    origin = "LogsScreenDetailViewModel.findLogById",
                    stacktrace = error
                )
            )
            return@launch
        }

        if (id == null) {
            Log.e(TAG, "ID of log is null")
            _logsState.value = LogsDetailState.NotFound
            return@launch
        }


        val log = withContext(Dispatchers.Default) {
            val log = logsDao.getLogById(id) ?: return@withContext null
            Log.i(TAG, "Found log with ID $id and formatting")
            val dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            return@withContext LogReportUiItem(
                log.id.toString(),
                dateFormat.format(log.startTime),
                log.isSuccess,
                log.connectedDevices.toTypedArray(),
                log.stackTrace ?: ""
            )
        }

        if (log == null) {
            Log.e(TAG, "Log was not found in the database")
            _logsState.value = LogsDetailState.NotFound
            return@launch
        }

        Log.i(TAG, "Showing log with ID: $id")
        _logsState.value = LogsDetailState.Loaded(log)
    }

    companion object {
        const val TAG = "LogsScreenDetailViewModel"
    }

}