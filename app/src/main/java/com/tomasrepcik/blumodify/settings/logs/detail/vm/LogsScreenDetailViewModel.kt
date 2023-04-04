package com.tomasrepcik.blumodify.settings.logs.detail.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import javax.inject.Inject

@HiltViewModel
class LogsScreenDetailViewModel @Inject constructor(private val logsDao: LogsDao) :
    ViewModel() {

    private val _logsState: MutableStateFlow<LogDetailState> = MutableStateFlow(LogDetailState.Loading)
    var logsState = _logsState.asStateFlow()

    fun findLogById(id: Int?, error: String?) = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Searching for log with ID $id")

        if (error != null){
            Log.e(TAG, "Error occurred during searching for correct ID")
            _logsState.value = LogDetailState.Error(error)
        }

        if (id == null){
            Log.e(TAG, "ID of log is null")
            _logsState.value = LogDetailState.NotFound
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
                log.connectedDevices,
                log.stackTrace
            )
        }

        if (log == null){
            Log.e(TAG, "Log was not found in the database")
            _logsState.value = LogDetailState.NotFound
            return@launch
        }
        Log.i(TAG, "Showing log with ID: $id")
        _logsState.value = LogDetailState.Loaded(log)
    }

    companion object {
        const val TAG = "LogsScreenDetailViewModel"
    }

}