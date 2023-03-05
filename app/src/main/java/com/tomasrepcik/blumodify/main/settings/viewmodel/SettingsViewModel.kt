package com.tomasrepcik.blumodify.main.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.tomasrepcik.blumodify.main.settings.model.TrackedDevicePickerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(): ViewModel() {

    private val _trackedDevicePickerState: MutableStateFlow<TrackedDevicePickerState> =
        MutableStateFlow(TrackedDevicePickerState.Loading)
    var trackedDevicePickerState = _trackedDevicePickerState.asStateFlow()
}