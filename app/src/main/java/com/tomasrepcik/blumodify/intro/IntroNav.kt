package com.tomasrepcik.blumodify.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.tomasrepcik.blumodify.intro.composables.MotivationScreen
import com.tomasrepcik.blumodify.intro.composables.RecommendationScreen
import com.tomasrepcik.blumodify.intro.composables.WelcomeScreen

fun NavGraphBuilder.introGraph(navController: NavController) {
    navigation(startDestination = IntroNav.INTRO_WELCOME_SCREEN, route = IntroNav.INTRO_ROUTE) {
        composable(IntroNav.INTRO_WELCOME_SCREEN){
            WelcomeScreen(navController)
        }
        composable(IntroNav.INTRO_MOTIVATION_SCREEN){
            MotivationScreen(navController)
        }
        composable(IntroNav.INTRO_RECOMMENDATION_SCREEN){
            RecommendationScreen(navController)
        }
    }
}

object IntroNav {
    const val INTRO_ROUTE = "intro"
    const val INTRO_WELCOME_SCREEN = "welcome"
    const val INTRO_MOTIVATION_SCREEN = "motivation"
    const val INTRO_RECOMMENDATION_SCREEN = "recommendation"
}
