package com.tomasrepcik.blumodify.main

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation(startDestination = MainNav.MAIN_SCREEN, route = MainNav.MAIN_ROUTE) {
        composable(MainNav.MAIN_SCREEN){
            Text(text = "Main")
        }
        composable(MainNav.MAIN_SETTINGS_SCREEN){
            Text(text = "Settings")
        }
        composable(MainNav.MAIN_ABOUT_SCREEN){
            Text(text = "About")
        }
    }
}

object MainNav {
    const val MAIN_ROUTE = "main"
    const val MAIN_SCREEN = "main_screen"
    const val MAIN_SETTINGS_SCREEN = "settings"
    const val MAIN_ABOUT_SCREEN = "about_screen"
}
