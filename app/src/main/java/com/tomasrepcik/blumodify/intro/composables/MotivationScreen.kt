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
import com.tomasrepcik.blumodify.intro.IntroNav
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme


@Composable
fun MotivationScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Motivation")
        Button(onClick = {
            navController.navigate(IntroNav.INTRO_RECOMMENDATION_SCREEN)
        }) {
            Text("Go to recommendations")
        }
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Go Back")
        }
    }
}

@Preview
@Composable
fun MotivationPreview() {
    val navController = rememberNavController()
    BluModifyTheme {
        MotivationScreen(navController = navController)
    }
}

