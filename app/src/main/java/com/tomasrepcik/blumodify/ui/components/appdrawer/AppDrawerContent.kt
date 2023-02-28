@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.tomasrepcik.blumodify.ui.components.appdrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.main.MainNav
import kotlinx.coroutines.launch

@Composable
fun AppDrawerContent(
    drawerState: DrawerState,
    navController: NavController,
    menuItems: List<AppDrawerItemInfo>
) {
    var currentPick by remember { mutableStateOf(DrawerOption.Home) }
    val coroutineScope = rememberCoroutineScope()

    Surface(color = MaterialTheme.colorScheme.onPrimary) {
        Column(modifier = Modifier
            .fillMaxHeight()
            .width(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppDrawerTitle()
            LazyColumn(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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