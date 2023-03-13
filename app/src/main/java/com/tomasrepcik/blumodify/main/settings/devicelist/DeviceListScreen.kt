package com.tomasrepcik.blumodify.main.settings.devicelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.main.settings.SettingsNavOption
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.main.settings.devicelist.ui.NoTrackedDevices
import com.tomasrepcik.blumodify.main.settings.devicelist.viewmodel.DeviceListState
import com.tomasrepcik.blumodify.main.settings.devicelist.viewmodel.DeviceListViewModel
import com.tomasrepcik.blumodify.main.settings.shared.ui.DeviceAction
import com.tomasrepcik.blumodify.main.settings.shared.ui.DevicePickerComp
import com.tomasrepcik.blumodify.app.ui.components.BackButton
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBarAction

@Composable
fun DeviceListScreen(
    navController: NavHostController,
    vm: DeviceListViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        vm.onLaunch()
    }

    Scaffold(
        topBar = {
            AppBar(
                title = R.string.settings_tracked_devices,
                navigationIcon = {
                    BackButton() {
                        navController.popBackStack()
                    }
                },
                appBarActions = arrayListOf(
                    AppBarAction(
                        R.drawable.ic_add,
                        R.string.settings_bt_picker
                    ) {
                        navController.navigate(SettingsNavOption.SettingsBtPicker.name)
                    }
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (val state = vm.listState.collectAsState().value) {
                is DeviceListState.Devices -> DevicePickerComp(
                    dataItems = state.devices,
                    DeviceAction.DELETE
                ) { device ->
                    vm.onDeviceDelete(device)
                }
                DeviceListState.Empty -> NoTrackedDevices {
                    navController.navigate(SettingsNavOption.SettingsBtPicker.name)
                }
                DeviceListState.Loading -> LoadingComp()
            }
        }
    }
}