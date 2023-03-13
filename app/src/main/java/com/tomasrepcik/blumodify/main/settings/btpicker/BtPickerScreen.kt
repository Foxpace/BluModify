package com.tomasrepcik.blumodify.main.settings.btpicker

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.BackButton
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBarAction
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.main.settings.btpicker.ui.*
import com.tomasrepcik.blumodify.main.settings.btpicker.viewmodel.BtPickerViewModel
import com.tomasrepcik.blumodify.main.settings.btpicker.viewmodel.TrackedDevicesState
import com.tomasrepcik.blumodify.main.settings.shared.ui.DeviceAction
import com.tomasrepcik.blumodify.main.settings.shared.ui.DevicePickerComp

@Composable
fun SettingsBtPickerScreen(navController: NavController, vm: BtPickerViewModel = hiltViewModel()) {

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        vm.onLaunch(context)
    }
    DisposableEffect(key1 = Unit){
        onDispose {
            vm.onDispose()
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = R.string.settings_bt_picker,
                navigationIcon = {
                    BackButton {
                        navController.popBackStack()
                    }
                },
                appBarActions = arrayListOf(
                    AppBarAction(R.drawable.ic_refresh, R.string.ic_refresh){
                      vm.onLaunch(context)
                    },
                    AppBarAction(R.drawable.ic_bt, R.string.ic_bt_permission){
                        val intentOpenBluetoothSettings = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                        context.startActivity(intentOpenBluetoothSettings)
                    }
                )
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (val state = vm.trackedDevicesPickerState.collectAsState().value) {
                TrackedDevicesState.Loading -> LoadingComp()
                is TrackedDevicesState.DevicesToAdd -> DevicePickerComp(state.devices, DeviceAction.ADD) { btDeviceToPick ->
                    vm.onDevicePick(btDeviceToPick)
                }
                TrackedDevicesState.NoDeviceToAdd -> NoDeviceComp()
                TrackedDevicesState.RequireBtOn -> TurnOnBtComp {
                    vm.onBtOn()
                }
                TrackedDevicesState.RequirePermission -> PermissionComp{
                    vm.onBtPermissionGranted()
                }
                TrackedDevicesState.AllDevicesAdded -> AllDevicesAddedComp()
            }
        }
    }
}