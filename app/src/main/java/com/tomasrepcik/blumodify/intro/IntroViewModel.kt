package com.tomasrepcik.blumodify.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.storage.AppCacheState
import com.tomasrepcik.blumodify.storage.AppCacheTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                when(it){
                    AppCacheState.Error -> {
                        _isLoading.value = false
                        _isOnboarded.value = false
                    }
                    is AppCacheState.Loaded -> {
                        _isLoading.value = false
                        _isOnboarded.value = it.settings.onboarded
                    }
                    AppCacheState.Loading -> {
                        _isLoading.value = true
                        _isOnboarded.value = true
                    }
                }
            }
        }

        viewModelScope.launch {
            appCache.loadInCache()
        }
    }


    fun saveUserOnboarding() {
        viewModelScope.launch(context = Dispatchers.IO) {
            appCache.storeOnboarding(true)
        }
    }
}