package com.tomasrepcik.blumodify.storage.cache

import kotlinx.coroutines.flow.StateFlow

interface AppCacheTemplate<T> {

    val state: StateFlow<T>

    suspend fun loadInCache()
    suspend fun storeOnboarding(isOnboarded: Boolean)

}