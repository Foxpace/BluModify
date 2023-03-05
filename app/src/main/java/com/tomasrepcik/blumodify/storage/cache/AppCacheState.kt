package com.tomasrepcik.blumodify.storage.cache

sealed class AppCacheState {
    object Loading: AppCacheState()
    class Loaded(val settings: AppSettings): AppCacheState()
    object Error: AppCacheState()
}
