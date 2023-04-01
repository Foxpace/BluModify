package com.tomasrepcik.blumodify.settings.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.SettingsNavOption
import com.tomasrepcik.blumodify.settings.settings.ui.SettingsClickableComp
import com.tomasrepcik.blumodify.settings.settings.ui.SettingsGroup
import com.tomasrepcik.blumodify.settings.settings.ui.SettingsSwitchComp

@Composable
fun SettingsScreen(
    navController: NavHostController,
    drawerState: DrawerState,
    vm: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            AppBar(
                drawerState = drawerState,
                title = R.string.drawer_settings,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp)
        ) {
            SettingsGroup(name = R.string.settings_devices) {
                SettingsSwitchComp(
                    name = R.string.settings_advanced_tracking,
                    icon = R.drawable.ic_bt,
                    iconDesc = R.string.ic_bt,
                    state = vm.advancedSettings.collectAsState()
                ) {
                    vm.toggleAdvancedSettings()
                }

                if (vm.advancedSettings.collectAsState().value){
                    SettingsClickableComp(
                        name = R.string.settings_advanced_tracking_dialog_button,
                        icon = R.drawable.ic_question_mark,
                        iconDesc = R.string.ic_question_mark
                    ) {
                        navController.navigate(SettingsNavOption.SettingsAdvancedExplanation.name)
                    }
                    SettingsClickableComp(
                        name = R.string.settings_devices_list,
                        icon = R.drawable.ic_check,
                        iconDesc = R.string.ic_check
                    ) {
                        navController.navigate(SettingsNavOption.SettingsDeviceList.name)
                    }
                }
            }

            SettingsGroup(name = R.string.settings_history) {
                SettingsClickableComp(
                    name = R.string.settings_logs,
                    icon = R.drawable.ic_history_reset,
                    iconDesc = R.string.settings_logs_button_description,
                ) {
                    navController.navigate(SettingsNavOption.SettingsLogsScreen.name)
                }
            }
        }
    }
}

@AllScreenPreview
@Composable
fun SettingsScreenPreview() {
    val navigator = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    BluModifyTheme {
        SettingsScreen(navigator, drawerState)
    }
}