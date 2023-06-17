package com.tomasrepcik.blumodify.settings.logs.list.vm


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.settings.logs.list.LogReportUiListItem
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
class LogsScreenViewModel @Inject constructor(private val logsDao: LogsDao) :
    ViewModel() {

    private val _logsState: MutableStateFlow<LogsState> = MutableStateFlow(LogsState.Loading)
    var logsState = _logsState.asStateFlow()

    fun onEvent(event: LogsEvent): Job = when(event){
        LogsEvent.OnLaunch -> loadLogs()
        LogsEvent.OnReverse -> reverseList()
    }

    private fun loadLogs(): Job = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Loading all logs and formatting")
        val logs = withContext(Dispatchers.Default) {
            val logReports =  logsDao.getAll()
            val dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            return@withContext logReports.map { logReport -> LogReportUiListItem(
                id = logReport.id,
                time = dateFormat.format(logReport.startTime),
                isSuccess = logReport.isSuccess,
                connectedDevices = logReport.connectedDevices.size.toString()
            ) }.toList()
        }

        if (logs.isEmpty()){
            Log.i(TAG, "Logs are empty")
            _logsState.value = LogsState.NoLogs
            return@launch
        }
        Log.i(TAG, "Showing logs in list")
        _logsState.value = LogsState.Logs(logs)
    }

    private fun reverseList(): Job = viewModelScope.launch(Dispatchers.Default) {
        when (val state = _logsState.value) {
            is LogsState.Logs -> {
                val reversed = state.logs.reversed()
                withContext(Dispatchers.Main) {
                    _logsState.value = LogsState.Logs(reversed)
                }
            }
            else -> Log.w(TAG, "No logs to reverse order")
        }
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }

}