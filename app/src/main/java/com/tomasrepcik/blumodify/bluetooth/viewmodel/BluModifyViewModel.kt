package com.tomasrepcik.blumodify.bluetooth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.notifications.NotificationRepoTemplate
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManagerTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BluModifyViewModel @Inject constructor(
    private val btWorkManager: BtWorkManagerTemplate,
    private val btController: BtControllerTemplate,
    private val btDeviceDao: BtDeviceDao,
    private val notificationRepo: NotificationRepoTemplate
) : ViewModel() {

    private val _bluModifyState: MutableStateFlow<BluModifyState> =
        MutableStateFlow(BluModifyState.Loading)
    val blumodifyState = _bluModifyState.asStateFlow()

    fun onEvent(event: BluModifyEvent): Job {
        Log.i(TAG, "Obtained event: $event")
        return when (event) {
            BluModifyEvent.OnLaunch -> onLaunch()
            BluModifyEvent.OnMainButtonClickEvent -> onButtonClicked()
            BluModifyEvent.OnError -> onRestart()
            BluModifyEvent.OnPermissionGranted -> onButtonClicked()
            BluModifyEvent.OnPermissionDenied -> onPermissionDenied()
        }
    }

    private fun onLaunch() = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Main on launch was called")
        _bluModifyState.value = BluModifyState.Loading
        checkCurrentState()
    }

    private fun onPermissionDenied() = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Permission was denied by user")
        _bluModifyState.value = BluModifyState.MissingPermission
    }

    private fun onButtonClicked() = viewModelScope.launch(Dispatchers.Main) {
        Log.i(TAG, "Main button was clicked")
        when (blumodifyState.value) {
            is BluModifyState.TurnedOff -> turnOn()
            is BluModifyState.TurnedOn -> turnOff()
            else -> {} // nothing to do - wait / resolve error
        }
    }

    private suspend fun checkCurrentState(): BluModifyState = withContext(Dispatchers.Main) {
        Log.i(TAG, "Checking current state of the service")
        when (val state = btWorkManager.workersWork()) {
            is AppResult.Error -> {
                Log.e(TAG, "Error occurred in the service")
                _bluModifyState.value = BluModifyState.ErrorOccurred(state)
                return@withContext BluModifyState.ErrorOccurred(state)
            }

            is AppResult.Success -> {
                if (state.data) {
                    _bluModifyState.value = BluModifyState.TurnedOn
                    return@withContext BluModifyState.TurnedOn
                } else {
                    _bluModifyState.value = BluModifyState.TurnedOff
                    return@withContext BluModifyState.TurnedOff
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
        if (!btController.isPermission() || !notificationRepo.isPermission()) {
            Log.w(TAG, "Missing permission to launch the app")
            _bluModifyState.value = BluModifyState.MissingPermission
            return@withContext
        }
        btWorkManager.initWorkers()
        checkCurrentState()
    }

    private suspend fun turnOff() = withContext(Dispatchers.Main) {
        Log.i(TAG, "Turning off the worker")
        btWorkManager.disposeWorkers()
        withContext(Dispatchers.Default) {
            btDeviceDao.resetDevices()
        }
        checkCurrentState()
    }

    companion object {
        const val TAG = "BluModifyModel"
    }
}