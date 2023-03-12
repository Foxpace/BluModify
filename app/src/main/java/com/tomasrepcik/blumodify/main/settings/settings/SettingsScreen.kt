package com.tomasrepcik.blumodify.main.settings.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.main.settings.SettingsNavOption

@Composable
fun SettingsScreen(
    navController: NavHostController,
    drawerState: DrawerState,
) {

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AppBar(
                drawerState = drawerState,
                title = R.string.drawer_settings,
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(it)
                .padding(16.dp)
        ) {
            SettingsGroup(name = R.string.settings_devices) {
                SettingsClickableComp(
                    name = R.string.settings_devices_list,
                    icon = R.drawable.ic_bt,
                    iconDesc = R.string.ic_bt
                ) {
                    navController.navigate(SettingsNavOption.SettingsDeviceList.name)
                }
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