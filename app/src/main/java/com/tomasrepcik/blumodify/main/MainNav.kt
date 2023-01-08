package com.tomasrepcik.blumodify.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.main.about.AboutScreen
import com.tomasrepcik.blumodify.main.home.HomeScreen
import com.tomasrepcik.blumodify.main.settings.SettingsScreen

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation(startDestination = MainNav.MAIN_HOME_SCREEN, route = MainNav.MAIN_ROUTE) {
        composable(MainNav.MAIN_HOME_SCREEN){
            HomeScreen(navController)
        }
        composable(MainNav.MAIN_SETTINGS_SCREEN){
            SettingsScreen()
        }
        composable(MainNav.MAIN_ABOUT_SCREEN){
            AboutScreen()
        }
    }
}

object MainNav {
    const val MAIN_ROUTE = "main"
    const val MAIN_HOME_SCREEN = "home_screen"
    const val MAIN_SETTINGS_SCREEN = "settings"
    const val MAIN_ABOUT_SCREEN = "about_screen"
}
