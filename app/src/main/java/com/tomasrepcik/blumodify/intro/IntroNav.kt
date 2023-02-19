package com.tomasrepcik.blumodify.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.intro.composables.RecommendationScreen
import com.tomasrepcik.blumodify.intro.composables.WelcomeScreen
import com.tomasrepcik.blumodify.intro.composables.motivation.MotivationEnergyScreen
import com.tomasrepcik.blumodify.intro.composables.motivation.MotivationPrivacyScreen

fun NavGraphBuilder.introGraph(navController: NavController) {
    navigation(startDestination = IntroNav.INTRO_WELCOME_SCREEN, route = IntroNav.INTRO_ROUTE) {
        composable(IntroNav.INTRO_WELCOME_SCREEN){
            WelcomeScreen(navController)
        }
        composable(IntroNav.INTRO_MOTIVATION_ENERGY_SCREEN){
            MotivationEnergyScreen(navController)
        }
        composable(IntroNav.INTRO_MOTIVATION_PRIVACY_SCREEN){
            MotivationPrivacyScreen(navController)
        }
        composable(IntroNav.INTRO_RECOMMENDATION_SCREEN){
            RecommendationScreen(navController)
        }
    }
}

object IntroNav {
    const val INTRO_ROUTE = "intro"
    const val INTRO_WELCOME_SCREEN = "welcome"
    const val INTRO_MOTIVATION_PRIVACY_SCREEN = "motivation_privacy"
    const val INTRO_MOTIVATION_ENERGY_SCREEN = "motivation_energy"
    const val INTRO_RECOMMENDATION_SCREEN = "recommendation"
}
