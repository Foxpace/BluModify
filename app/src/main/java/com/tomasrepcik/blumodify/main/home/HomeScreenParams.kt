package com.tomasrepcik.blumodify.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.main.home.appdrawer.AppDrawerItemInfo
import com.tomasrepcik.blumodify.main.home.appdrawer.DrawerOption
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            Icons.Rounded.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
            )
        }

    ) { paddingValues ->
        Surface {
            Column(modifier = Modifier.padding(paddingValues)) {

            }
        }
    }
}


object HomeScreenParams {
    val drawerOptions = arrayListOf(
        AppDrawerItemInfo(DrawerOption.Home, R.string.drawer_home),
        AppDrawerItemInfo(DrawerOption.Settings, R.string.drawer_settings),
        AppDrawerItemInfo(DrawerOption.About, R.string.drawer_about)
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    HomeScreen(navController = navController, drawerState = drawerState)
}