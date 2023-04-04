package com.tomasrepcik.blumodify.settings

import android.util.Log
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
            var id: Int?
            var error: String? = null
            try{
                id = it.arguments?.getString("id")?.toInt()
            } catch (e: Exception) {
                id = null
                error = e.stackTraceToString()
            }
            LogsScreenDetail(navController, id, error)
        }
    }
}

object SettingsNav {

    fun goToLogScreenWithDetails(navigator: NavController, id: Int?){
        Log.i("SettingsNav", "Log with ID $id was picked")
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
