package com.tomasrepcik.blumodify

import androidx.compose.material3.DrawerState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.about.AboutScreen
import com.tomasrepcik.blumodify.home.HomeScreen
import com.tomasrepcik.blumodify.settings.settings.SettingsScreen

fun NavGraphBuilder.mainGraph(navController: NavHostController, drawerState: DrawerState) {
    navigation(startDestination = MainNavOption.HomeScreen.name, route = NavRoutes.MainRoute.name) {
        composable(MainNavOption.HomeScreen.name){
            HomeScreen(navController, drawerState)
        }
        composable(MainNavOption.SettingsScreen.name){
            SettingsScreen(navController, drawerState)
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
