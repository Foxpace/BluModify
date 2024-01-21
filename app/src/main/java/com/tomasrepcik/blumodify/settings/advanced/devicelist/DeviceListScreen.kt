package com.tomasrepcik.blumodify.settings.advanced.devicelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.BackButton
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBarAction
import com.tomasrepcik.blumodify.app.ui.components.error.ErrorScreen
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.SettingsNavOption
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListEvent
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListState
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import com.tomasrepcik.blumodify.settings.advanced.shared.ui.DeviceAction
import com.tomasrepcik.blumodify.settings.advanced.shared.ui.DevicePickerComp

@Composable
fun DeviceListScreen(
    navController: NavHostController, state: DeviceListState, onEvent: (DeviceListEvent) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        onEvent(DeviceListEvent.OnLaunch)
    }

    Scaffold(topBar = {
        AppBar(
            title = R.string.settings_tracked_devices, navigationIcon = {
                BackButton {
                    navController.popBackStack()
                }
            }, appBarActions = arrayOf(AppBarAction(
                R.drawable.ic_add,
                R.string.settings_bt_picker,
            ) {
                navController.navigate(SettingsNavOption.SettingsBtPicker.name)
            })
        )
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (state) {
                is DeviceListState.Devices -> DevicePickerComp(
                    dataItems = state.devices, action = DeviceAction.DELETE
                ) { device ->
                    onEvent(DeviceListEvent.OnDeviceDelete(device))
                }

                DeviceListState.Empty -> ErrorScreen<DeviceListState>(explanation = R.string.settings_no_tracked_device,
                    primaryText = R.string.settings_bt_picker,
                    onPrimaryClick = {
                        navController.navigate(SettingsNavOption.SettingsBtPicker.name)
                    })

                DeviceListState.Loading -> LoadingComp()
            }
        }
    }
}

@AllScreenPreview
@Composable
fun AllDevicesAddedPreview() {
    BluModifyTheme {
        Surface {
            DeviceListScreen(
                navController = rememberNavController(),
                state = DeviceListState.Devices(listOf(BtItem("Device", "00:00:00:00")))
            ) {}
        }
    }
}