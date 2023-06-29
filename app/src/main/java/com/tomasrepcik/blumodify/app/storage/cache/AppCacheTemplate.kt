package com.tomasrepcik.blumodify.app.storage.cache

import kotlinx.coroutines.flow.StateFlow

interface AppCacheTemplate<T> {

    val state: StateFlow<T>
    suspend fun loadInCacheAsync()
    suspend fun loadInCacheSync(): AppSettings
    suspend fun storeOnboarding(isOnboarded: Boolean)
    suspend fun storeAdvancedSettings(isAdvancedSettings: Boolean)
    suspend fun setDefault()

}