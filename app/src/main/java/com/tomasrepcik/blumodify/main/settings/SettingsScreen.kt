package com.tomasrepcik.blumodify.main.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.main.settings.SettingsNavOption
import com.tomasrepcik.blumodify.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.ui.components.appbar.AppBarAction
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun SettingsScreen(
    navController: NavHostController,
    drawerState: DrawerState,
) {
    Scaffold(
        topBar = {
            AppBar(
                drawerState = drawerState,
                title = R.string.settings_tracked_devices,
                appBarActions = arrayListOf(
                    AppBarAction(
                        R.drawable.ic_add,
                        R.string.settings_tracked_button_description
                    ) {
                        navController.navigate(SettingsNavOption.SettingsBtPicker.name)
                    }
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            LazyColumn {

            }
        }
    }
}

@AllScreenPreview
@Composable
fun SettingsScreenPreview() {
    val navigator = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    BluModifyTheme {
        SettingsScreen(navigator, drawerState)
    }
}