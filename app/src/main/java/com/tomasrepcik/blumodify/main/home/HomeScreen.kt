package com.tomasrepcik.blumodify.main.home

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

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home")
        Button(onClick = {
            navController.navigate(MainNav.MAIN_SETTINGS_SCREEN)
        }) {
            Text("Settings")
        }
        Button(onClick = {
            navController.navigate(MainNav.MAIN_ABOUT_SCREEN)
        }) {
            Text("About")
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}