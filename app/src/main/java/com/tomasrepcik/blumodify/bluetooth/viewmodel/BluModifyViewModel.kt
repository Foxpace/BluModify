package com.tomasrepcik.blumodify.bluetooth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.app.storage.room.BtDeviceDao
import com.tomasrepcik.blumodify.bluetooth.model.BlumodifyState
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManagerTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BluModifyViewModel @Inject constructor(
    private val btWorkManagerTemplate: BtWorkManagerTemplate,
    private val btDeviceDao: BtDeviceDao,
    private val appCache: AppCacheTemplate<AppCacheState>
) : ViewModel() {

    private val _bluModifyState: MutableStateFlow<BlumodifyState> =
        MutableStateFlow(BlumodifyState.Loading)
    val blumodifyState = _bluModifyState.asStateFlow()

    private var uuid: UUID? = null

    init {
        viewModelScope.launch(Dispatchers.Default) {
            appCache.state.collect { cacheState ->
                when (cacheState) {
                    is AppCacheState.Loaded -> onCacheLoaded(cacheState.settings)
                    AppCacheState.Error -> onCacheFailed(cacheState)
                    AppCacheState.Loading -> {}
                }
            }
        }
    }

    fun onLaunch() = viewModelScope.launch(Dispatchers.Main) {
        _bluModifyState.value = BlumodifyState.Loading
        uuid?.let {
            checkCurrentState(it)
        }
    }

    fun onButtonClicked() = viewModelScope.launch(Dispatchers.Main) {
        when (val state = blumodifyState.value) {
            is BlumodifyState.TurnedOff -> turnOn(state.uuid)
            is BlumodifyState.TurnedOn -> turnOff(state.uuid)
            else -> {} // nothing to do - wait / resolve error / add device
        }
    }

    private suspend fun onCacheLoaded(settings: AppSettings) = withContext(Dispatchers.Default){
        var id = settings.uuid
        if (id.isEmpty()) {
            id = UUID.randomUUID().toString()
            appCache.storeWorkerId(id)
        }
        uuid = UUID.fromString(id)
        onLaunch()
    }

    private suspend fun onCacheFailed(cacheState: AppCacheState) = withContext(Dispatchers.Main){
        _bluModifyState.value = BlumodifyState.ErrorOccurred(
            AppResult.Error(
                "",
                ErrorCause.CACHE_FAILED,
                ""
            )
        )
    }

    private suspend fun checkCurrentState(uuid: UUID) = withContext(Dispatchers.Main) {
        val isEmpty = withContext(Dispatchers.Default) defaultContext@{
            val devices = btDeviceDao.getAll()
            return@defaultContext devices.isEmpty()
        }

        if (isEmpty) {
            _bluModifyState.value = BlumodifyState.NothingToTrack
            return@withContext
        }

        when (val state = btWorkManagerTemplate.workersWork(uuid)) {
            is AppResult.Error -> _bluModifyState.value = BlumodifyState.ErrorOccurred(state)
            is AppResult.Success -> {
                if (state.data) {
                    _bluModifyState.value = BlumodifyState.TurnedOn(uuid)
                } else {
                    _bluModifyState.value = BlumodifyState.TurnedOff(uuid)
                }
            }
        }
    }



    private suspend fun turnOn(oldUUID: UUID) = withContext(Dispatchers.Main) {
        _bluModifyState.value = BlumodifyState.Loading
        val newUUID = UUID.randomUUID()
        btWorkManagerTemplate.initWorkers(oldUUID, newUUID)
        withContext(Dispatchers.Default){
            uuid = newUUID
            appCache.storeWorkerId(newUUID.toString())
        }
        checkCurrentState(newUUID)
    }

    private suspend fun turnOff(uuid: UUID) = withContext(Dispatchers.Main) {
        _bluModifyState.value = BlumodifyState.Loading
        btWorkManagerTemplate.disposeWorkers(uuid)
        checkCurrentState(uuid)
    }
}