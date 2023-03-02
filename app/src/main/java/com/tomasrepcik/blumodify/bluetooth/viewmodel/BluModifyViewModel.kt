package com.tomasrepcik.blumodify.bluetooth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.bluetooth.controllers.blumodify.BluModifyControllerTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluModifyViewModel @Inject constructor(private val btController: BluModifyControllerTemplate): ViewModel() {

    private val _isRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    init {
        viewModelScope.launch {
            btController.isRunning.collect{
                _isRunning.value = it
            }
        }
        viewModelScope.launch {
            btController.initialize()
        }
    }

    fun toggleBt()  {
        viewModelScope.launch {
            if (_isRunning.value) {
                disposeBtController()
            } else {
                initBtController()
            }
        }
    }

    private suspend fun initBtController(){
        btController.initialize()
    }

    private suspend fun disposeBtController(){
        btController.dispose()
    }



}