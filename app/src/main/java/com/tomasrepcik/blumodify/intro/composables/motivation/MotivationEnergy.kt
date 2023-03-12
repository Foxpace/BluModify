package com.tomasrepcik.blumodify.intro.composables.motivation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.intro.IntroNavOption
import com.tomasrepcik.blumodify.intro.composables.IntroCompose
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme


@Composable
fun MotivationEnergyScreen(navController: NavController) = IntroCompose(
    navController = navController,
    image = R.drawable.ic_motivation_energy,
    imageDescription = R.string.motivation_energy_image,
    textTitle = R.string.motivation_energy_title,
    textDescription = R.string.motivation_energy_text,
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

