package com.tomasrepcik.blumodify

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.app.ui.components.appdrawer.AppDrawerContent
import com.tomasrepcik.blumodify.app.ui.components.appdrawer.AppDrawerItemInfo
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.intro.introGraph
import com.tomasrepcik.blumodify.settings.settingsGraph


@Composable
fun MainCompose(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    isOnboarded: Boolean
) {
    BluModifyTheme {
        Surface {
            ModalNavigationDrawer(
                gesturesEnabled = isOnboarded,
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        AppDrawerContent(
                            drawerState = drawerState,
                            menuItems = DrawerParams.drawerButtons,
                            defaultPick = MainNavOption.HomeScreen
                        ) { onUserPickedOption ->
                            when (onUserPickedOption) {
                                MainNavOption.HomeScreen -> {
                                    navController.navigate(onUserPickedOption.name) {
                                        popUpTo(NavRoutes.MainRoute.name)
                                    }
                                }
                                MainNavOption.SettingsScreen -> {
                                    navController.navigate(onUserPickedOption.name) {
                                        popUpTo(NavRoutes.MainRoute.name)
                                    }
                                }
                                MainNavOption.AboutScreen -> {
                                    navController.navigate(onUserPickedOption.name) {
                                        popUpTo(NavRoutes.MainRoute.name)
                                    }
                                }
                            }
                        }
                    }

                }
            ) {
                NavHost(
                    navController,
                    startDestination = if (isOnboarded) NavRoutes.MainRoute.name else NavRoutes.IntroRoute.name
                ) {
                    introGraph(navController)
                    mainGraph(navController, drawerState)
                    settingsGraph(navController)
                }
            }
        }
    }
}

enum class NavRoutes {
    IntroRoute,
    MainRoute,
    SettingsRoute
}

object DrawerParams {
    val drawerButtons = arrayListOf(
        AppDrawerItemInfo(
            MainNavOption.HomeScreen,
            R.string.drawer_home,
            R.drawable.ic_home,
            R.string.drawer_home_description
        ),
        AppDrawerItemInfo(
            MainNavOption.SettingsScreen,
            R.string.drawer_settings,
            R.drawable.ic_settings,
            R.string.drawer_settings_description
        ),
        AppDrawerItemInfo(
            MainNavOption.AboutScreen,
            R.string.drawer_about,
            R.drawable.ic_info,
            R.string.drawer_info_description
        )
    )
}