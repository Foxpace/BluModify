package com.tomasrepcik.blumodify.bluetooth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.storage.controllers.bluetooth.BtController
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
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
    private val btController: BtController,
    private val btDeviceDao: BtDeviceDao
) : ViewModel() {

    private val _bluModifyState: MutableStateFlow<BlumodifyState> =
        MutableStateFlow(BlumodifyState.Loading)
    val blumodifyState = _bluModifyState.asStateFlow()

    fun onEvent(event: BluModifyEvent) {
        when (event) {
            BluModifyEvent.OnLaunch -> onLaunch()
            BluModifyEvent.OnMainButtonClickEvent -> onButtonClicked()
            BluModifyEvent.OnError -> onRestart()
            BluModifyEvent.OnPermissionGranted -> onButtonClicked()
        }
    }

    private fun onLaunch() = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Main on launch was called")
        _bluModifyState.value = BlumodifyState.Loading
        checkCurrentState()
    }

    private fun onButtonClicked() = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Main button was clicked")
        when (blumodifyState.value) {
            is BlumodifyState.TurnedOff -> turnOn()
            is BlumodifyState.TurnedOn -> turnOff()
            else -> {} // nothing to do - wait / resolve error
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

    private fun onRestart() = viewModelScope.launch {
        Log.i(TAG, "Restarting service")
        turnOff()
        turnOn()
    }


    private suspend fun turnOn() = withContext(Dispatchers.Main) {
        Log.i(TAG, "Turning on the worker")
        if (!btController.isPermission()) {
            Log.w(TAG, "Missing permission to launch the app")
            _bluModifyState.value = BlumodifyState.MissingPermission
            return@withContext
        }
        btWorkManagerTemplate.initWorkers()
        checkCurrentState()
    }

    private suspend fun turnOff() = withContext(Dispatchers.Main) {
        Log.i(TAG, "Turning off the worker")
        btWorkManagerTemplate.disposeWorkers()
        withContext(Dispatchers.Default) {
            btDeviceDao.resetDevices()
        }
        checkCurrentState()
    }

    companion object {
        const val TAG = "BluModifyModel"
    }
}