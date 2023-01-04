package com.tomasrepcik.blumodify.intro

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.introGraph(navController: NavController) {
    navigation(startDestination = IntroNav.INTRO_WELCOME_SCREEN, route = IntroNav.INTRO_ROUTE) {
        composable(IntroNav.INTRO_WELCOME_SCREEN){
            Text(text = "Welcome")
        }
        composable(IntroNav.INTRO_MOTIVATION_SCREEN){
            Text(text = "Motivation")
        }
        composable(IntroNav.INTRO_ENDING_SCREEN){
            Text(text = "Ending")
        }
    }
}

object IntroNav {
    const val INTRO_ROUTE = "intro"
    const val INTRO_WELCOME_SCREEN = "welcome"
    const val INTRO_MOTIVATION_SCREEN = "motivation"
    const val INTRO_ENDING_SCREEN = "ending"
}
