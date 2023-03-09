package com.tomasrepcik.blumodify.main.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.NavRoutes
import com.tomasrepcik.blumodify.main.MainNavOption
import com.tomasrepcik.blumodify.main.settings.ui.btpicker.SettingsBtPickerScreen

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation(
        startDestination = MainNavOption.SettingsScreen.name,
        route = NavRoutes.SettingsRoute.name
    ) {
        composable(SettingsNavOption.SettingsBtPicker.name) {
            SettingsBtPickerScreen(navController)
        }
    }
}

enum class SettingsNavOption {
    SettingsBtPicker
}
