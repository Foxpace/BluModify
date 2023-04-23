package com.tomasrepcik.blumodify.settings

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.MainNavOption
import com.tomasrepcik.blumodify.NavRoutes
import com.tomasrepcik.blumodify.settings.advanced.btpicker.SettingsBtPickerScreen
import com.tomasrepcik.blumodify.settings.advanced.btpicker.vm.BtPickerViewModel
import com.tomasrepcik.blumodify.settings.advanced.devicelist.DeviceListScreen
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListViewModel
import com.tomasrepcik.blumodify.settings.advanced.explanation.AdvancedExplanationScreen
import com.tomasrepcik.blumodify.settings.logs.detail.LogsScreenDetail
import com.tomasrepcik.blumodify.settings.logs.list.LogsScreen
import com.tomasrepcik.blumodify.settings.logs.list.vm.LogsScreenViewModel

fun NavGraphBuilder.settingsGraph(navController: NavHostController) {
    navigation(
        startDestination = MainNavOption.SettingsScreen.name, route = NavRoutes.SettingsRoute.name
    ) {
        composable(SettingsNavOption.SettingsDeviceList.name) {
            val vm: DeviceListViewModel = hiltViewModel()
            DeviceListScreen(navController, vm.listState.collectAsState().value) {
                vm.onEvent(it)
            }
        }
        composable(SettingsNavOption.SettingsBtPicker.name) {
            val vm: BtPickerViewModel = hiltViewModel()
            SettingsBtPickerScreen(
                navController,
                vm.trackedDevicesPickerState.collectAsState().value
            ) {
                vm.onEvent(it)
            }
        }
        composable(SettingsNavOption.SettingsAdvancedExplanation.name) {
            AdvancedExplanationScreen(navController)
        }
        composable(SettingsNavOption.SettingsLogsScreen.name) {
            val vm: LogsScreenViewModel = hiltViewModel()
            LogsScreen(navController, vm.logsState.collectAsState().value){
                vm.onEvent(it)
            }
        }
        composable("${SettingsNavOption.SettingsLogsScreenDetail.name}/{id}") {
            var id: Int?
            var error: String? = null
            try {
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

    fun goToLogScreenWithDetails(navigator: NavController, id: Int?) {
        Log.i("SettingsNav", "Log with ID $id was picked")
        navigator.navigate("${SettingsNavOption.SettingsLogsScreenDetail.name}/$id")
    }
}

enum class SettingsNavOption {
    SettingsDeviceList, SettingsAdvancedExplanation, SettingsBtPicker, SettingsLogsScreen, SettingsLogsScreenDetail
}
