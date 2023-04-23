package com.tomasrepcik.blumodify.settings.settings.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.settings.SettingsNavOption
import com.tomasrepcik.blumodify.settings.settings.states.SettingsEvent
import com.tomasrepcik.blumodify.settings.settings.states.SettingsState
import com.tomasrepcik.blumodify.settings.settings.ui.SettingsClickableComp
import com.tomasrepcik.blumodify.settings.settings.ui.SettingsGroup
import com.tomasrepcik.blumodify.settings.settings.ui.SettingsSwitchComp

@Composable
fun SettingsLoadedComp(navController: NavController, state: SettingsState.SettingsLoaded, onEvent: (SettingsEvent) -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        SettingsGroup(name = R.string.settings_devices) {
            SettingsSwitchComp(
                name = R.string.settings_advanced_tracking,
                icon = R.drawable.ic_bt,
                iconDesc = R.string.ic_bt,
                state = state.settings.isAdvancedSettings
            ) {
                onEvent(SettingsEvent.ToggleAdvancedSettings)
            }

            if (state.settings.isAdvancedSettings){
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