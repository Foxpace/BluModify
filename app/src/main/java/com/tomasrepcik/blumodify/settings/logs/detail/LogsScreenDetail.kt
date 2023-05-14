package com.tomasrepcik.blumodify.settings.logs.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.BackButton
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.error.comp.ErrorComp
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.settings.logs.detail.screens.LogDetailComp
import com.tomasrepcik.blumodify.settings.logs.detail.vm.LogDetailState
import com.tomasrepcik.blumodify.settings.logs.detail.vm.LogsScreenDetailViewModel

@Composable
fun LogsScreenDetail(
    navController: NavHostController,
    id: Int?,
    error: String?,
    vm: LogsScreenDetailViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        vm.findLogById(id, error)
    }

    Scaffold(topBar = {
        AppBar(title = R.string.settings_advanced_tracking_dialog_title, navigationIcon = {
            BackButton {
                navController.popBackStack()
            }
        })
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = vm.logsState.collectAsState().value) {
                is LogDetailState.Loaded -> LogDetailComp(state.log)
                LogDetailState.Loading -> LoadingComp()
                is LogDetailState.Error -> ErrorComp<Error>(
                    explanation = R.string.settings_empty_log,
                    primaryText = R.string.back,
                    // TODO: add detail
                    onPrimaryClick = { navController.popBackStack() }) {

                }
                LogDetailState.NotFound -> ErrorComp<Error>(
                    explanation = R.string.settings_empty_log,
                    primaryText = R.string.back,
                    onPrimaryClick = { navController.popBackStack() }) {

                }
            }
        }
    }
}

