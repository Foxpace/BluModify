package com.tomasrepcik.blumodify.intro.composables

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.intro.IntroNav
import com.tomasrepcik.blumodify.intro.IntroViewModel
import com.tomasrepcik.blumodify.main.MainNav
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview

@Composable
fun RecommendationScreen(
    navController: NavController,
    viewModel: IntroViewModel = hiltViewModel()
) = IntroCompose(
    navController = navController,
    image = R.drawable.ic_bt_inactive,
    imageDescription = R.string.recommendation_image,
    textTitle = R.string.recommendation_title,
    textDescription = R.string.recommendation_text,
    buttonText = R.string.start_app
) {
    viewModel.saveUserOnboarding()
    navController.navigate(MainNav.MAIN_ROUTE) {
        popUpTo(IntroNav.INTRO_ROUTE)
    }
}

@AllScreenPreview
@Composable
fun RecommendationScreenPreview() {
    val navController = rememberNavController()
    RecommendationScreen(navController = navController)
}