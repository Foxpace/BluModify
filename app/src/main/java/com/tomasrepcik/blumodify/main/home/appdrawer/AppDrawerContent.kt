package com.tomasrepcik.blumodify.main.home.appdrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.main.MainNav
import kotlinx.coroutines.launch

@Composable
fun AppDrawerContent(drawerState: DrawerState, navController: NavController, menuItems: List<AppDrawerItemInfo>) {
    var currentPick by remember { mutableStateOf(DrawerOption.Home) }
    val coroutineScope = rememberCoroutineScope()
    Surface(color = MaterialTheme.colorScheme.onPrimary) {
        Column(modifier = Modifier.fillMaxHeight()) {
            AppDrawerTitle()
            LazyColumn {
                items(menuItems) { item ->
                    AppDrawerItem(item = item) {

                        if (currentPick == it) {
                            return@AppDrawerItem
                        }
                        currentPick = it
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        when (it) {
                            DrawerOption.Home -> {
                                navController.navigate(MainNav.MAIN_HOME_SCREEN) {
                                    popUpTo(MainNav.MAIN_ROUTE)
                                }
                            }
                            DrawerOption.Settings -> {
                                navController.navigate(MainNav.MAIN_SETTINGS_SCREEN) {
                                    popUpTo(MainNav.MAIN_ROUTE)
                                }
                            }
                            DrawerOption.About -> {
                                navController.navigate(MainNav.MAIN_ABOUT_SCREEN) {
                                    popUpTo(MainNav.MAIN_ROUTE)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}