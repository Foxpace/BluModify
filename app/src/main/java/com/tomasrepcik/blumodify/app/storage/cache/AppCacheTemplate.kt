package com.tomasrepcik.blumodify.app.storage.cache

import kotlinx.coroutines.flow.StateFlow

interface AppCacheTemplate<T> {

    val state: StateFlow<T>
    suspend fun loadInCache()
    suspend fun storeOnboarding(isOnboarded: Boolean)

}