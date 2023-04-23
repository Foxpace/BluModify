package com.tomasrepcik.blumodify.app.storage.cache

import androidx.datastore.core.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppCache @Inject constructor(private val dataStore: DataStore<AppSettings>) :
    AppCacheTemplate<AppCacheState> {

    private val _cacheState = MutableStateFlow<AppCacheState>(AppCacheState.Loading)
    override var state = _cacheState.asStateFlow()

    override suspend fun loadInCacheAsync() {
        dataStore.data.collect {
            _cacheState.value = AppCacheState.Loaded(it)
        }
    }

    override suspend fun loadInCacheSync(): AppSettings = withContext(Dispatchers.IO) {
        val settings = dataStore.data.first()
        _cacheState.value = AppCacheState.Loaded(settings)
        return@withContext settings
    }

    override suspend fun storeOnboarding(isOnboarded: Boolean): Unit = withContext(Dispatchers.IO) {
        dataStore.updateData { actualSettings: AppSettings ->
            actualSettings.copy(isOnboarded = isOnboarded)
        }
    }

    override suspend fun storeAdvancedSettings(isAdvancedSettings: Boolean): Unit =
        withContext(Dispatchers.IO) {
            dataStore.updateData { actualSettings: AppSettings ->
                actualSettings.copy(isAdvancedSettings = isAdvancedSettings)
            }
        }


}