package com.tomasrepcik.blumodify.settings.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.settings.settings.states.SettingsEvent
import com.tomasrepcik.blumodify.settings.settings.states.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val appCache: AppCacheTemplate<AppCacheState>) :
    ViewModel() {


    private val _settingsState: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState.SettingsLoading)
    var settingsState = _settingsState.asStateFlow()

    private val _settings: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings.default)

    init {
        viewModelScope.launch {
            appCache.state.collect {
                when (it) {
                    AppCacheState.Error -> {
                        _settingsState.value = SettingsState.SettingsError
                    }
                    is AppCacheState.Loaded -> {
                        _settings.value = it.settings
                        _settingsState.value = SettingsState.SettingsLoaded(it.settings)
                    }
                    else -> {
                        Log.i(TAG, ": Loading the settings")
                    }
                }
            }
        }
    }

    fun onEvent(settingsEvent: SettingsEvent) {
        when(settingsEvent){
            SettingsEvent.OnError -> Unit
            SettingsEvent.ToggleAdvancedSettings -> toggleAdvancedSettings()
        }
    }

    private fun toggleAdvancedSettings() {
        viewModelScope.launch {
            appCache.storeAdvancedSettings(_settings.value.isAdvancedSettings.not())
        }
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }

}