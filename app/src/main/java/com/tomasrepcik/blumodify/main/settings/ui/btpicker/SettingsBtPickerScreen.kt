

package com.tomasrepcik.blumodify.main.settings.ui.btpicker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.main.settings.model.TrackedDevicePickerState
import com.tomasrepcik.blumodify.main.settings.ui.btpicker.states.*
import com.tomasrepcik.blumodify.main.settings.viewmodel.SettingsViewModel
import com.tomasrepcik.blumodify.ui.components.appbar.AppBar

@Composable
fun SettingsBtPickerScreen(navController: NavController, vm: SettingsViewModel = hiltViewModel()) {
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
                            contentDescription = stringResource(id = R.string.drawer_menu_description)
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            LoadingComp()
            when (val state = vm.trackedDevicePickerState.collectAsState().value) {
                TrackedDevicePickerState.Loading -> LoadingComp()
                is TrackedDevicePickerState.DevicesToAdd -> PickDeviceComp()
                TrackedDevicePickerState.NoDeviceToAdd -> NoDeviceComp()
                TrackedDevicePickerState.RequireBtOn -> TurnOnBtComp()
                TrackedDevicePickerState.RequirePermission -> PermissionComp()
            }
        }

    }

}