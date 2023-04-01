package com.tomasrepcik.blumodify.settings.logs.detail.vm

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

    fun findLogById(id: Int?) = viewModelScope.launch(Dispatchers.Main) {

        if (id == null){
            _logsState.value = LogDetailState.NotFound
            return@launch
        }


        val log = withContext(Dispatchers.Default) {
            val log = logsDao.getLogById(id) ?: return@withContext null
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
            _logsState.value = LogDetailState.NotFound
            return@launch
        }

        _logsState.value = LogDetailState.Loaded(log)
    }

}