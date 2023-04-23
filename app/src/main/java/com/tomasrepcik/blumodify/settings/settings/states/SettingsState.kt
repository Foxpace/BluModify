package com.tomasrepcik.blumodify.settings.settings.states

import com.tomasrepcik.blumodify.app.storage.cache.AppSettings

sealed class SettingsState {

    class SettingsLoaded(val settings: AppSettings): SettingsState()
    object SettingsLoading: SettingsState()
    object SettingsError: SettingsState()

}
