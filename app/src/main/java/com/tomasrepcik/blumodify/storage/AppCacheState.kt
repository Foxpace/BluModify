package com.tomasrepcik.blumodify.storage

import com.tomasrepcik.blumodify.storage.datastore.AppSettings

sealed class AppCacheState {
    object Loading: AppCacheState()
    class Loaded(val settings: AppSettings): AppCacheState()
    object Error: AppCacheState()
}
