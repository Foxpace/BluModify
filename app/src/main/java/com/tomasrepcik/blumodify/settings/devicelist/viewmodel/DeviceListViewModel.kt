package com.tomasrepcik.blumodify.settings.devicelist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasrepcik.blumodify.app.storage.room.BtDeviceDao
import com.tomasrepcik.blumodify.settings.shared.model.BtDeviceToPick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val db: BtDeviceDao
) : ViewModel() {
    private val _listState: MutableStateFlow<DeviceListState> =
        MutableStateFlow(DeviceListState.Loading)
    var listState = _listState.asStateFlow()

    fun onLaunch(){
        viewModelScope.launch {
            refreshDevices()
        }
    }

    fun onDeviceDelete(device: BtDeviceToPick) {
        viewModelScope.launch {
            deleteDevice(device)
            refreshDevices()
        }
    }

    private suspend fun refreshDevices(){
        withContext(Dispatchers.Main){
            _listState.value = DeviceListState.Loading
        }
        val devices = withContext(Dispatchers.Default){
            return@withContext db.getAll().map { BtDeviceToPick(it.name, it.macAddress) }
        }
        withContext(Dispatchers.Main){
            if (devices.isEmpty()){
                _listState.value = DeviceListState.Empty
                return@withContext
            }
            _listState.value = DeviceListState.Devices(devices = devices)

        }
    }


    private suspend fun deleteDevice(device: BtDeviceToPick){
        withContext(Dispatchers.Default){
            db.deleteByMacAddress(device.macAddress)
        }
    }

}