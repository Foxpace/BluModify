package com.tomasrepcik.blumodify.settings.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.error.comp.ErrorComp
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.settings.screens.SettingsLoadedComp
import com.tomasrepcik.blumodify.settings.settings.states.SettingsEvent
import com.tomasrepcik.blumodify.settings.settings.states.SettingsState

@Composable
fun SettingsScreen(
    navController: NavHostController,
    drawerState: DrawerState,
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit
) {
    Scaffold(topBar = {
        AppBar(
            drawerState = drawerState,
            title = R.string.drawer_settings,
        )
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                SettingsState.SettingsError -> ErrorComp<SettingsState>(explanation = R.string.drawer_settings,
                    onClick = { onEvent(SettingsEvent.OnError) })

                is SettingsState.SettingsLoaded -> SettingsLoadedComp(
                    navController = navController, state = state
                ) {
                    onEvent(it)
                }

                SettingsState.SettingsLoading -> LoadingComp()
            }
        }
    }
}

@AllScreenPreview
@Composable
fun SettingsScreenPreview() {
    val navigator = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val state = SettingsState.SettingsLoaded(AppSettings.default)
    BluModifyTheme {
        SettingsScreen(navigator, drawerState, state) {}
    }
}