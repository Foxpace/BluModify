package com.tomasrepcik.blumodify.bluetooth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.storage.room.BtDeviceDao
import com.tomasrepcik.blumodify.bluetooth.model.BlumodifyState
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManagerTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BluModifyViewModel @Inject constructor(
    private val btWorkManagerTemplate: BtWorkManagerTemplate,
    private val btDeviceDao: BtDeviceDao
    ) : ViewModel() {

    private val _bluModifyState: MutableStateFlow<BlumodifyState> =
        MutableStateFlow(BlumodifyState.Loading)
    val blumodifyState = _bluModifyState.asStateFlow()

    fun onLaunch() = viewModelScope.launch(Dispatchers.Main) {
        _bluModifyState.value = BlumodifyState.Loading
        checkCurrentState()
    }

    fun onButtonClicked() = viewModelScope.launch(Dispatchers.Main) {
        when (blumodifyState.value) {
            is BlumodifyState.TurnedOff -> turnOn()
            is BlumodifyState.TurnedOn -> turnOff()
            else -> {} // nothing to do - wait / resolve error / add device
        }
    }

    private suspend fun checkCurrentState() = withContext(Dispatchers.Main) {
        val isEmpty = withContext(Dispatchers.Default) defaultContext@{
            val devices = btDeviceDao.getAll()
            return@defaultContext devices.isEmpty()
        }

        if (isEmpty) {
            _bluModifyState.value = BlumodifyState.NothingToTrack
            return@withContext
        }

        when (val state = btWorkManagerTemplate.workersWork()) {
            is AppResult.Error -> _bluModifyState.value = BlumodifyState.ErrorOccurred(state)
            is AppResult.Success -> {
                if (state.data) {
                    _bluModifyState.value = BlumodifyState.TurnedOn
                } else {
                    _bluModifyState.value = BlumodifyState.TurnedOff
                }
            }
        }
    }

    private suspend fun turnOn() = withContext(Dispatchers.Main) {
        _bluModifyState.value = BlumodifyState.Loading
        btWorkManagerTemplate.initWorkers()
        checkCurrentState()
    }

    private suspend fun turnOff() = withContext(Dispatchers.Main) {
        _bluModifyState.value = BlumodifyState.Loading
        btWorkManagerTemplate.disposeWorkers()
        withContext(Dispatchers.Default){
            btDeviceDao.resetDevices()
        }
        checkCurrentState()
    }
}