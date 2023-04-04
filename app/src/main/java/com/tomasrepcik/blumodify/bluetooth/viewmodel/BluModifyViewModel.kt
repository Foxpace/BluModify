package com.tomasrepcik.blumodify.bluetooth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
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

    private suspend fun checkCurrentState(): BlumodifyState = withContext(Dispatchers.Main) {
        Log.i(TAG, "Checking current state of the service")
        when (val state = btWorkManagerTemplate.workersWork()) {
            is AppResult.Error -> {
                Log.e(TAG, "Error occurred in the service")
                _bluModifyState.value = BlumodifyState.ErrorOccurred(state)
                return@withContext BlumodifyState.ErrorOccurred(state)
            }
            is AppResult.Success -> {
                if (state.data) {
                    _bluModifyState.value = BlumodifyState.TurnedOn
                    return@withContext BlumodifyState.TurnedOn
                } else {
                    _bluModifyState.value = BlumodifyState.TurnedOff
                    return@withContext BlumodifyState.TurnedOff
                }
            }
        }
    }

    suspend fun restart() = withContext(Dispatchers.Main) {
        when (checkCurrentState()) {
            BlumodifyState.TurnedOn -> {
                Log.i(TAG, "Restarting running service")
                turnOff()
                turnOn()
            }
            else -> {
                Log.w(TAG, "Tried to restart, but the service is not running")
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

    companion object {
        const val TAG = "BluModifyModel"
    }
}