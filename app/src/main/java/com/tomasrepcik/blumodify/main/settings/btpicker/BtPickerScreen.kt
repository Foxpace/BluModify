

package com.tomasrepcik.blumodify.main.settings.btpicker

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.main.settings.btpicker.model.TrackedDevicesState
import com.tomasrepcik.blumodify.main.settings.btpicker.ui.states.*
import com.tomasrepcik.blumodify.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.ui.components.appbar.AppBarAction

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
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painterResource(id = R.drawable.ic_back),
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = stringResource(id = R.string.drawer_menu_description)
                        )
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
                is TrackedDevicesState.DevicesToAdd -> PickDeviceComp(state) { btDeviceToPick ->
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