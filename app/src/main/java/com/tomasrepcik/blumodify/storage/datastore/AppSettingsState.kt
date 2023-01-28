package com.tomasrepcik.blumodify.storage.datastore

sealed class AppSettingsState {

    object Loading: AppSettingsState()
    class Loaded(val settings: AppSettings): AppSettingsState()

}
