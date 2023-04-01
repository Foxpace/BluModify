package com.tomasrepcik.blumodify.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.MainNavOption
import com.tomasrepcik.blumodify.NavRoutes
import com.tomasrepcik.blumodify.settings.advanced.btpicker.SettingsBtPickerScreen
import com.tomasrepcik.blumodify.settings.advanced.devicelist.DeviceListScreen
import com.tomasrepcik.blumodify.settings.advanced.explanation.AdvancedExplanationScreen
import com.tomasrepcik.blumodify.settings.logs.detail.LogsScreenDetail
import com.tomasrepcik.blumodify.settings.logs.list.LogsScreen

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation(
        startDestination = MainNavOption.SettingsScreen.name,
        route = NavRoutes.SettingsRoute.name
    ) {
        composable(SettingsNavOption.SettingsDeviceList.name) {
            DeviceListScreen(navController)
        }
        composable(SettingsNavOption.SettingsBtPicker.name) {
            SettingsBtPickerScreen(navController)
        }
        composable(SettingsNavOption.SettingsAdvancedExplanation.name) {
            AdvancedExplanationScreen(navController)
        }
        composable(SettingsNavOption.SettingsLogsScreen.name) {
            LogsScreen(navController)
        }
        composable("${SettingsNavOption.SettingsLogsScreenDetail.name}/{id}") {
            val id = it.arguments?.getInt(SettingsNav.SettingsLogsScreenDetailId)
            LogsScreenDetail(navController, id)
        }
    }
}

object SettingsNav {

    fun goToLogScreenWithDetails(navigator: NavController, id: Int?){
        navigator.navigate("${SettingsNavOption.SettingsLogsScreenDetail.name}/$id")
    }

    const val SettingsLogsScreenDetailId = "SettingsLogsScreenDetailId"
}

enum class SettingsNavOption {
    SettingsDeviceList,
    SettingsAdvancedExplanation,
    SettingsBtPicker,
    SettingsLogsScreen,
    SettingsLogsScreenDetail
}
