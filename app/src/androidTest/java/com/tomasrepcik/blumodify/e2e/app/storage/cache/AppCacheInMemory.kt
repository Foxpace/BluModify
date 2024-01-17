package com.tomasrepcik.blumodify.e2e.app.storage.cache

import androidx.datastore.core.DataStore
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppCacheInMemory @Inject constructor(private val dataStore: DataStore<AppSettings>) :
    AppCacheTemplate<AppCacheState> {
    private val _cacheState: MutableStateFlow<AppCacheState.Loaded> =
        MutableStateFlow(AppCacheState.Loaded(AppSettings.default))
    override var state = _cacheState.asStateFlow()

    override suspend fun loadInCacheAsync() = withContext(Dispatchers.Main) {
        val data = dataStore.data.first()
        _cacheState.value = AppCacheState.Loaded(
            AppSettings(
                isOnboarded = data.isOnboarded,
                isAdvancedSettings = data.isAdvancedSettings
            )
        )
    }

    override suspend fun loadInCacheSync(): AppSettings = runBlocking {
        val settings = AppSettings(
            isOnboarded = dataStore.data.first().isOnboarded,
            isAdvancedSettings = dataStore.data.first().isAdvancedSettings
        )
        _cacheState.value = AppCacheState.Loaded(settings)
        return@runBlocking settings
    }

    override suspend fun storeOnboarding(isOnboarded: Boolean) {
        _cacheState.value =
            AppCacheState.Loaded(AppSettings.default.copy(isOnboarded = isOnboarded))
    }

    override suspend fun storeAdvancedSettings(isAdvancedSettings: Boolean) {
        _cacheState.value =
            AppCacheState.Loaded(_cacheState.value.settings.copy(isAdvancedSettings = isAdvancedSettings))
    }

    override suspend fun setDefault() {
        _cacheState.value = AppCacheState.Loaded(AppSettings.default)
    }


}