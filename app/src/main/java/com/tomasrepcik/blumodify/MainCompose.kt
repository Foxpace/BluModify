package com.tomasrepcik.blumodify

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.intro.IntroNav
import com.tomasrepcik.blumodify.intro.introGraph
import com.tomasrepcik.blumodify.main.mainGraph
import com.tomasrepcik.blumodify.ui.theme.AppTheme

@Composable
fun MainCompose() {
    val navController = rememberNavController()
    AppTheme {
        NavHost(navController, startDestination = IntroNav.INTRO_ROUTE) {
            introGraph(navController)
            mainGraph(navController)
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    MainCompose()
}