package com.tomasrepcik.blumodify.intro.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.intro.IntroNavOption
import com.tomasrepcik.blumodify.intro.IntroTestTags
import com.tomasrepcik.blumodify.intro.composables.ui.IntroCompose


@Composable
fun MotivationEnergyScreen(navController: NavController) = IntroCompose(
    navController = navController,
    image = R.drawable.ic_motivation_energy,
    imageDescription = R.string.motivation_energy_image,
    textTitle = R.string.motivation_energy_title,
    textDescription = R.string.motivation_energy_text,
    buttonTestTag = IntroTestTags.INTRO_MOTIVATION_ENERGY_SCREEN_NEXT_BUTTON
) {
    navController.navigate(IntroNavOption.MotivationPrivacyScreen.name)
}

@AllScreenPreview
@Composable
fun MotivationEnergyPreview() {
    val navController = rememberNavController()
    BluModifyTheme {
        MotivationEnergyScreen(navController = navController)
    }
}

