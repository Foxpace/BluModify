package com.tomasrepcik.blumodify.app.storage.cache

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AppCache @Inject constructor(private val dataStore: DataStore<AppSettings>) :
    AppCacheTemplate<AppCacheState> {

    private val _cacheState = MutableStateFlow<AppCacheState>(AppCacheState.Loading)
    override var state = _cacheState.asStateFlow()

    override suspend fun loadInCache() {
        dataStore.data.collect {
            _cacheState.value = AppCacheState.Loaded(it)
        }
    }

    override suspend fun storeOnboarding(isOnboarded: Boolean) {
        dataStore.updateData { actualSettings: AppSettings ->
            actualSettings.copy(onboarded = isOnboarded)
        }
    }

    override suspend fun storeWorkerId(id: String) {
        dataStore.updateData { actualSettings: AppSettings ->
            actualSettings.copy(uuid = id)
        }
    }


}