package com.tomasrepcik.blumodify.storage

import androidx.datastore.core.DataStore
import com.tomasrepcik.blumodify.storage.datastore.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AppCacheImp @Inject constructor(private val dataStore: DataStore<AppSettings>) :
    AppCache<AppCacheState> {

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


}