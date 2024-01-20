package com.tomasrepcik.blumodify.intro.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.intro.IntroNavOption
import com.tomasrepcik.blumodify.intro.composables.ui.IntroCompose

@Composable
fun RecommendationScreen(
    navController: NavController,
) = IntroCompose(
    navController = navController,
    image = R.drawable.ic_bt_inactive,
    imageDescription = R.string.recommendation_image,
    textTitle = R.string.recommendation_title,
    textDescription = R.string.recommendation_text,
    buttonText = R.string.next,
) {
    navController.navigate(IntroNavOption.BatteryOptimisation.name)
}

@AllScreenPreview
@Composable
fun RecommendationScreenPreview() {
    BluModifyTheme {
        val navController = rememberNavController()
        RecommendationScreen(navController = navController)
    }
}