package com.tomasrepcik.blumodify.settings.logs.list.vm


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.settings.logs.list.LogUiListItem
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
class LogsListViewModel @Inject constructor(private val logsDao: LogsDao) :
    ViewModel() {

    private val _logsState: MutableStateFlow<LogsListState> = MutableStateFlow(LogsListState.Loading)
    var logsState = _logsState.asStateFlow()

    fun onEvent(event: LogsListEvent): Job = when(event){
        LogsListEvent.OnLaunch -> loadLogs()
        LogsListEvent.OnReverse -> reverseList()
    }

    private fun loadLogs(): Job = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Loading all logs and formatting")
        val logs = withContext(Dispatchers.Default) {
            val logReports =  logsDao.getAll()
            val dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            return@withContext logReports.map { logReport -> LogUiListItem(
                id = logReport.id,
                time = dateFormat.format(logReport.startTime),
                isSuccess = logReport.isSuccess,
                connectedDevices = logReport.connectedDevices.size.toString()
            ) }.toList()
        }

        if (logs.isEmpty()){
            Log.i(TAG, "Logs are empty")
            _logsState.value = LogsListState.NoLogs
            return@launch
        }
        Log.i(TAG, "Showing logs in list")
        _logsState.value = LogsListState.Logs(logs)
    }

    private fun reverseList(): Job = viewModelScope.launch(Dispatchers.Default) {
        when (val state = _logsState.value) {
            is LogsListState.Logs -> {
                val reversed = state.logs.reversed()
                withContext(Dispatchers.Main) {
                    _logsState.value = LogsListState.Logs(reversed)
                }
            }
            else -> Log.w(TAG, "No logs to reverse order")
        }
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }

}