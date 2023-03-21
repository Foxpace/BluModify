package com.tomasrepcik.blumodify.settings.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val appCache: AppCacheTemplate<AppCacheState>) :
    ViewModel() {


    private val _isAdvancedSettings: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var advancedSettings = _isAdvancedSettings.asStateFlow()

    private val _isError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isError = _isError.asStateFlow()

    private val _settings: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings.default)
    var settings = _settings.asStateFlow()

    init {
        viewModelScope.launch {
            appCache.state.collect {
                when (it) {
                    AppCacheState.Error -> {
                        _isError.value = true
                    }
                    is AppCacheState.Loaded -> {
                        _settings.value = it.settings
                        _isAdvancedSettings.value = it.settings.isAdvancedSettings
                    }
                    else -> {
                        Log.i(TAG, ": Loading the settings")
                    }
                }
            }
        }
    }

    fun toggleAdvancedSettings() {
        val toggledValue = !_isAdvancedSettings.value
        _isAdvancedSettings.value = toggledValue
        viewModelScope.launch {
            appCache.storeAdvancedSettings(toggledValue)
        }
    }

    companion object {
        const val TAG = "SettingsViewModel"
    }

}