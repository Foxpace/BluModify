package com.tomasrepcik.blumodify.settings.settings.states

sealed class SettingsEvent {

    object ToggleAdvancedSettings: SettingsEvent()
    object OnError: SettingsEvent()

}
