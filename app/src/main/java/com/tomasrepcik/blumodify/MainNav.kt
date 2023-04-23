package com.tomasrepcik.blumodify

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.about.AboutScreen
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyViewModel
import com.tomasrepcik.blumodify.home.HomeScreen
import com.tomasrepcik.blumodify.settings.settings.SettingsScreen
import com.tomasrepcik.blumodify.settings.settings.SettingsViewModel

fun NavGraphBuilder.mainGraph(navController: NavHostController, drawerState: DrawerState) {
    navigation(startDestination = MainNavOption.HomeScreen.name, route = NavRoutes.MainRoute.name) {
        composable(MainNavOption.HomeScreen.name){
            val vm: BluModifyViewModel = hiltViewModel()
            HomeScreen(drawerState, vm.blumodifyState.collectAsState().value){
                vm.onEvent(it)
            }
        }
        composable(MainNavOption.SettingsScreen.name){
            val vm: SettingsViewModel = hiltViewModel()
            SettingsScreen(navController, drawerState, vm.settingsState.collectAsState().value){
                vm.onEvent(it)
            }
        }
        composable(MainNavOption.AboutScreen.name){
            AboutScreen(drawerState)
        }
    }
}

enum class MainNavOption {
    HomeScreen,
    AboutScreen,
    SettingsScreen,
}
