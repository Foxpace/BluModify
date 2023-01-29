package com.tomasrepcik.blumodify.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.storage.AppCache
import com.tomasrepcik.blumodify.storage.AppCacheState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IntroViewModel @Inject constructor(private val appCache: AppCache<AppCacheState>): ViewModel() {

    private val _isLoaded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLoaded = _isLoaded.asStateFlow()

    private val _isOnboarded: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isOnboarded = _isOnboarded.asStateFlow()

    init {

        viewModelScope.launch {
            appCache.state.collect{
                when(it){
                    AppCacheState.Error -> {
                        _isLoaded.value = true
                        _isOnboarded.value = false
                    }
                    is AppCacheState.Loaded -> {
                        _isLoaded.value = true
                        _isOnboarded.value = it.settings.onboarded
                    }
                    AppCacheState.Loading -> {
                        _isLoaded.value = false
                        _isOnboarded.value = false
                    }
                }
            }
        }

        viewModelScope.launch(context = Dispatchers.IO) {
            appCache.loadInCache()
        }
    }


    fun saveUserOnboarding() {
        viewModelScope.launch(context = Dispatchers.IO) {
            appCache.storeOnboarding(true)
        }
    }
}