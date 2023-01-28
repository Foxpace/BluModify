package com.tomasrepcik.blumodify

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.intro.IntroNav
import com.tomasrepcik.blumodify.intro.IntroViewModel
import com.tomasrepcik.blumodify.intro.introGraph
import com.tomasrepcik.blumodify.main.mainGraph
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun MainCompose(
    navController: NavHostController = rememberNavController(),
    introViewModel: IntroViewModel = hiltViewModel()
) {
    BluModifyTheme {
        Surface {
//            val onboarded = introViewModel.userOnboarded.collectAsState()
            NavHost(navController, startDestination = IntroNav.INTRO_ROUTE) {
                introGraph(navController)
                mainGraph(navController)
            }
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    MainCompose()
}