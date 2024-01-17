package com.tomasrepcik.blumodify.settings.settings.states

import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings

sealed class SettingsState {

    data class SettingsLoaded(val settings: AppSettings): SettingsState()
    data object SettingsLoading: SettingsState()
    data class SettingsError(val error: AppResult.Error<ErrorCause>): SettingsState()

}
