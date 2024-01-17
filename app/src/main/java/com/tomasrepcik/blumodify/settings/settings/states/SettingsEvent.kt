package com.tomasrepcik.blumodify.settings.settings.states

sealed class SettingsEvent {

    data object ToggleAdvancedSettings: SettingsEvent()
    data object OnError: SettingsEvent()

}
