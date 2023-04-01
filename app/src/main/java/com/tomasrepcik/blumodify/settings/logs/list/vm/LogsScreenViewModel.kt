package com.tomasrepcik.blumodify.settings.logs.list.vm


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.settings.logs.list.LogReportUiListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    fun loadLogs() = viewModelScope.launch(Dispatchers.Main) {

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
            _logsState.value = LogsState.NoLogs
            return@launch
        }

        _logsState.value = LogsState.Logs(logs)
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }

}