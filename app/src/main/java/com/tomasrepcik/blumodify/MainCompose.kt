package com.tomasrepcik.blumodify

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.intro.IntroNav
import com.tomasrepcik.blumodify.intro.IntroViewModel
import com.tomasrepcik.blumodify.intro.introGraph
import com.tomasrepcik.blumodify.main.MainNav
import com.tomasrepcik.blumodify.main.home.HomeScreenParams
import com.tomasrepcik.blumodify.main.home.appdrawer.AppDrawerContent
import com.tomasrepcik.blumodify.main.mainGraph
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun MainCompose(
    navController: NavHostController = rememberNavController(),
    vm: IntroViewModel = hiltViewModel()
) {
    BluModifyTheme {
        Surface {
            val onboarded = vm.isOnboarded.collectAsState()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppDrawerContent(
                        drawerState = drawerState,
                        navController = navController,
                        menuItems = HomeScreenParams.drawerOptions
                    )
                }
            ) {
                NavHost(
                    navController,
                    startDestination = if (onboarded.value) MainNav.MAIN_ROUTE else IntroNav.INTRO_ROUTE
                ) {
                    introGraph(navController)
                    mainGraph(navController, drawerState)
                }
            }
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    MainCompose()
}