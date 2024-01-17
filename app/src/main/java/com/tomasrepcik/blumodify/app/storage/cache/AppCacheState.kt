package com.tomasrepcik.blumodify.app.storage.cache

import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause

sealed class AppCacheState {
    data object Loading: AppCacheState()
    class Loaded(val settings: AppSettings): AppCacheState()
    class Error(val error: AppResult.Error<ErrorCause>): AppCacheState()
}
