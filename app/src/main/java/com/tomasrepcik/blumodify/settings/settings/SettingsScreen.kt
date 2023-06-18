package com.tomasrepcik.blumodify.settings.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.error.ErrorScreen
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
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState) { data ->
            Snackbar(
                data,
                containerColor = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }, topBar = {
        AppBar(
            drawerState = drawerState,
            title = R.string.drawer_settings,
        )
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is SettingsState.SettingsError -> ErrorScreen(explanation = R.string.drawer_settings,
                    ignoreDetails = false,
                    error = state.error,
                    onPrimaryClick = {
                        onEvent(SettingsEvent.OnError)
                    })

                is SettingsState.SettingsLoaded -> SettingsLoadedComp(
                    navController = navController, state = state, snackbarHostState,
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