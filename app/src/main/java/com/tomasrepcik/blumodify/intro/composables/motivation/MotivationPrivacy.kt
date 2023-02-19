package com.tomasrepcik.blumodify.intro.composables.motivation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.intro.IntroNav
import com.tomasrepcik.blumodify.intro.composables.IntroCompose
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme


@Composable
fun MotivationPrivacyScreen(navController: NavController) = IntroCompose(
    navController = navController,
    image = R.drawable.ic_motivation_privacy,
    imageDescription = R.string.motivation_privacy_image,
    textTitle = R.string.motivation_privacy_title,
    textDescription = R.string.motivation_privacy_text,
) {
    navController.navigate(IntroNav.INTRO_RECOMMENDATION_SCREEN)
}

@AllScreenPreview
@Composable
fun MotivationPrivacyPreview() {
    val navController = rememberNavController()
    BluModifyTheme {
        MotivationPrivacyScreen(navController = navController)
    }
}

