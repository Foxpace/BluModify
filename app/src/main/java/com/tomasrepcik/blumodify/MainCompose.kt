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
import com.tomasrepcik.blumodify.main.mainGraph
import com.tomasrepcik.blumodify.ui.components.appdrawer.AppDrawerContent
import com.tomasrepcik.blumodify.ui.components.appdrawer.AppDrawerItemInfo
import com.tomasrepcik.blumodify.ui.components.appdrawer.DrawerOption
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
                        menuItems = DrawerParams.drawerOptions
                    )
                }
            ) {
                NavHost(
                    navController,
                    startDestination = if (onboarded.value) MainNav.MAIN_ROUTE else IntroNav.INTRO_ROUTE
                ) {
                    introGraph(navController)
                    mainGraph(drawerState)
                }
            }
        }
    }
}

object DrawerParams {
    val drawerOptions = arrayListOf(
        AppDrawerItemInfo(
            DrawerOption.Home,
            R.string.drawer_home,
            R.drawable.ic_home,
            R.string.drawer_home_description
        ),
        AppDrawerItemInfo(
            DrawerOption.Settings,
            R.string.drawer_settings,
            R.drawable.ic_settings,
            R.string.drawer_settings_description
        ),
        AppDrawerItemInfo(
            DrawerOption.About,
            R.string.drawer_about,
            R.drawable.ic_info,
            R.string.drawer_info_description
        )
    )
}

@Preview
@Composable
fun MainActivityPreview() {
    MainCompose()
}