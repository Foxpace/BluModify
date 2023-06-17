package com.tomasrepcik.blumodify.intro

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IntroViewModel @Inject constructor(private val appCache: AppCacheTemplate<AppCacheState>): ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLoading = _isLoading.asStateFlow()

    private val _isOnboarded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isOnboarded = _isOnboarded.asStateFlow()

    init {
        viewModelScope.launch {
            appCache.state.collect{
                onAppCacheState(it)
            }
        }

        viewModelScope.launch {
            appCache.loadInCacheAsync()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onAppCacheState(appCache: AppCacheState) {
        when(appCache){
            is AppCacheState.Error -> {
                _isLoading.value = false
                _isOnboarded.value = false
            }
            is AppCacheState.Loaded -> {
                _isLoading.value = false
                _isOnboarded.value = appCache.settings.isOnboarded
            }
            AppCacheState.Loading -> {
                _isLoading.value = true
                _isOnboarded.value = false
            }
        }
    }


    fun saveUserOnboarding(): Job = viewModelScope.launch {
        appCache.storeOnboarding(true)
    }
}