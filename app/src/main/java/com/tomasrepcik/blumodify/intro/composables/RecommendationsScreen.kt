package com.tomasrepcik.blumodify.intro.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.main.MainNav
import androidx.hilt.navigation.compose.hiltViewModel
import com.tomasrepcik.blumodify.intro.IntroNav
import com.tomasrepcik.blumodify.intro.IntroViewModel

@Composable
fun RecommendationScreen(navController: NavController, viewModel: IntroViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Recommendations")
        Button(onClick = {
            viewModel.saveUserOnboarding()
            navController.navigate(MainNav.MAIN_ROUTE) {
                popUpTo(IntroNav.INTRO_ROUTE)
            }
        }) {
            Text("Go to main")
        }
        Button(onClick = {
            navController.popBackStack(route = IntroNav.INTRO_WELCOME_SCREEN, inclusive = false)
        }) {
            Text("Go to beginning")
        }
    }

}

@Preview
@Composable
fun RecommendationScreenPreview() {
    val navController = rememberNavController()
    RecommendationScreen(navController = navController)
}