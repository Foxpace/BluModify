package com.tomasrepcik.blumodify.settings.logs.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.BackButton
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBarAction
import com.tomasrepcik.blumodify.app.ui.components.error.ErrorScreen
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.settings.SettingsNav
import com.tomasrepcik.blumodify.settings.logs.list.ui.LogsList
import com.tomasrepcik.blumodify.settings.logs.list.vm.LogsListEvent
import com.tomasrepcik.blumodify.settings.logs.list.vm.LogsListState

@Composable
fun LogsScreen(
    navController: NavHostController, state: LogsListState, onEvent: (LogsListEvent) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        onEvent(LogsListEvent.OnLaunch)
    }

    Scaffold(topBar = {
        AppBar(
            title = R.string.settings_logs, navigationIcon = {
                BackButton {
                    navController.popBackStack()
                }
            }, appBarActions = arrayOf(AppBarAction(
                R.drawable.ic_reverse, R.string.ic_reverse
            ) {
                onEvent(LogsListEvent.OnReverse)
            })

        )
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                LogsListState.Loading -> LoadingComp()
                is LogsListState.Logs -> LogsList(logs = state.logs) {
                    SettingsNav.goToLogScreenWithDetails(navController, it)
                }

                LogsListState.NoLogs -> ErrorScreen<LogsListState>(explanation = R.string.settings_no_logs_text,
                    primaryText = R.string.back,
                    onPrimaryClick = {
                        navController.popBackStack()
                    })
            }
        }
    }
}

