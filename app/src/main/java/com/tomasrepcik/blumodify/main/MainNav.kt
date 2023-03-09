package com.tomasrepcik.blumodify.main

import androidx.compose.material3.DrawerState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.NavRoutes
import com.tomasrepcik.blumodify.main.about.AboutScreen
import com.tomasrepcik.blumodify.main.home.HomeScreen
import com.tomasrepcik.blumodify.main.settings.ui.SettingsScreen

fun NavGraphBuilder.mainGraph(navController: NavHostController, drawerState: DrawerState) {
    navigation(startDestination = MainNavOption.HomeScreen.name, route = NavRoutes.MainRoute.name) {
        composable(MainNavOption.HomeScreen.name){
            HomeScreen(drawerState)
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
