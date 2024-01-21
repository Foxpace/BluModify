package com.tomasrepcik.blumodify.settings.advanced.btpicker

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.BackButton
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBarAction
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.settings.advanced.btpicker.ui.AllDevicesAddedComp
import com.tomasrepcik.blumodify.settings.advanced.btpicker.ui.NoDeviceComp
import com.tomasrepcik.blumodify.settings.advanced.btpicker.ui.PermissionComp
import com.tomasrepcik.blumodify.settings.advanced.btpicker.ui.TurnOnBtComp
import com.tomasrepcik.blumodify.settings.advanced.btpicker.vm.TrackedDevicesEvent
import com.tomasrepcik.blumodify.settings.advanced.btpicker.vm.TrackedDevicesState
import com.tomasrepcik.blumodify.settings.advanced.shared.ui.DeviceAction
import com.tomasrepcik.blumodify.settings.advanced.shared.ui.DevicePickerComp

@Composable
fun SettingsBtPickerScreen(
    navController: NavController,
    state: TrackedDevicesState,
    onEvent: (TrackedDevicesEvent) -> Unit
) {

    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        onEvent(TrackedDevicesEvent.OnLaunch)
    }
    DisposableEffect(key1 = Unit) {
        onDispose {
            onEvent(TrackedDevicesEvent.OnDispose)
        }
    }

    Scaffold(topBar = {
        AppBar(title = R.string.settings_bt_picker, navigationIcon = {
            BackButton {
                navController.popBackStack()
            }
        }, appBarActions = arrayOf(AppBarAction(
            R.drawable.ic_refresh,
            R.string.ic_refresh,
        ) {
            onEvent(TrackedDevicesEvent.OnLaunch)
        }, AppBarAction(
            R.drawable.ic_bt,
            R.string.ic_bt_permission,
        ) {
            val intentOpenBluetoothSettings = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            context.startActivity(intentOpenBluetoothSettings)
        }))
    }) {
        Column(modifier = Modifier.padding(it)) {
            when (state) {
                TrackedDevicesState.Loading -> LoadingComp()
                is TrackedDevicesState.DevicesToAdd -> DevicePickerComp(
                    state.devices,
                    DeviceAction.ADD
                ) { btDeviceToPick ->
                    onEvent(TrackedDevicesEvent.OnDevicePick(btDeviceToPick))
                }

                TrackedDevicesState.NoDeviceToAdd -> NoDeviceComp()
                TrackedDevicesState.RequireBtOn -> TurnOnBtComp {
                    onEvent(TrackedDevicesEvent.OnBtOn)
                }

                TrackedDevicesState.RequirePermission -> PermissionComp {
                    onEvent(TrackedDevicesEvent.OnPermissionGranted)
                }

                TrackedDevicesState.AllDevicesAdded -> AllDevicesAddedComp()
            }
        }
    }
}