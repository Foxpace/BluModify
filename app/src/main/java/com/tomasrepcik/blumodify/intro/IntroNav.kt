package com.tomasrepcik.blumodify.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.NavRoutes
import com.tomasrepcik.blumodify.intro.composables.RecommendationScreen
import com.tomasrepcik.blumodify.intro.composables.WelcomeScreen
import com.tomasrepcik.blumodify.intro.composables.MotivationEnergyScreen
import com.tomasrepcik.blumodify.intro.composables.MotivationPrivacyScreen

fun NavGraphBuilder.introGraph(navController: NavController) {
    navigation(startDestination = IntroNavOption.WelcomeScreen.name, route = NavRoutes.IntroRoute.name) {
        composable(IntroNavOption.WelcomeScreen.name){
            WelcomeScreen(navController)
        }
        composable(IntroNavOption.MotivationEnergyScreen.name){
            MotivationEnergyScreen(navController)
        }
        composable(IntroNavOption.MotivationPrivacyScreen.name){
            MotivationPrivacyScreen(navController)
        }
        composable(IntroNavOption.RecommendationScreen.name){
            RecommendationScreen(navController)
        }
    }
}

enum class IntroNavOption {
    WelcomeScreen,
    MotivationPrivacyScreen,
    MotivationEnergyScreen,
    RecommendationScreen
}
