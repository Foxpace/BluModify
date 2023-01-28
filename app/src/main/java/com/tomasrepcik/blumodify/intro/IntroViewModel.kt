package com.tomasrepcik.blumodify.intro

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.intro.model.UserOnboarded
import com.tomasrepcik.blumodify.storage.datastore.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IntroViewModel @Inject constructor(private val dataStore: DataStore<AppSettings>): ViewModel() {

    private val _userOnboarded = MutableStateFlow(UserOnboarded.Loading)
    val userOnboarded = _userOnboarded.asStateFlow()

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            dataStore.data.collect {
                Log.e("Splash", "Loaded in $it")
                processAppSettings(it)
            }
        }
    }


    fun saveUserOnboarding() {
        viewModelScope.launch {
            val newSettings = dataStore.updateData { actualSettings: AppSettings ->
                actualSettings.copy(onboarded = true)
            }
            processAppSettings(newSettings)
        }
    }

    private fun processAppSettings(appSettings: AppSettings) {
        _userOnboarded.value =
            if (appSettings.onboarded) UserOnboarded.Onboarded else UserOnboarded.NotOnboarded
    }
}