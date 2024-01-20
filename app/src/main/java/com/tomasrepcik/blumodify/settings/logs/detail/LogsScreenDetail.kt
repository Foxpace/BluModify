package com.tomasrepcik.blumodify.settings.logs.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.mail.MailSending
import com.tomasrepcik.blumodify.app.ui.components.BackButton
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBarAction
import com.tomasrepcik.blumodify.app.ui.components.error.ErrorScreen
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import com.tomasrepcik.blumodify.settings.logs.detail.screens.LogDetailComp
import com.tomasrepcik.blumodify.settings.logs.detail.vm.LogsDetailEvent
import com.tomasrepcik.blumodify.settings.logs.detail.vm.LogsDetailState

@Composable
fun LogsScreenDetail(
    navController: NavHostController,
    id: Int?,
    error: String?,
    state: LogsDetailState,
    onEvent: (LogsDetailEvent) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        onEvent(LogsDetailEvent.OnLaunch(id, error))
    }

    Scaffold(topBar = {
        AppBar(title = R.string.settings_advanced_tracking_dialog_title, navigationIcon = {
            BackButton {
                navController.popBackStack()
            }
        }, appBarActions = when (state) {
            is LogsDetailState.Loaded -> {
                val context = LocalContext.current
                if (state.log.isSuccess) {
                    arrayOf()
                } else {
                    arrayOf(AppBarAction(
                        icon = R.drawable.ic_mail,
                        description = R.string.ic_mail,
                    ) {
                        MailSending.reportLog(context, state.log)
                    })
                }
            }

            else -> arrayOf()
        })
    }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is LogsDetailState.Loaded -> LogDetailComp(state.log)
                LogsDetailState.Loading -> LoadingComp()
                is LogsDetailState.Error -> ErrorScreen(explanation = R.string.settings_empty_log,
                    ignoreDetails = false,
                    error = state.error,
                    primaryText = R.string.back,
                    onPrimaryClick = {
                        navController.popBackStack()
                    })

                LogsDetailState.NotFound -> ErrorScreen<Error>(
                    explanation = R.string.settings_empty_log,
                    primaryText = R.string.back,
                    onPrimaryClick = { navController.popBackStack() },
                )
            }
        }
    }
}

@AllScreenPreview
@Composable
fun LogsScreenDetailPreview() {
    BluModifyTheme {
        Surface {
            LogsScreenDetail(
                navController = rememberNavController(),
                id = 0,
                error = null,
                LogsDetailState.Loaded(
                    LogReportUiItem(
                        "0", "1.1.1999", true, arrayOf(BtItem("Device", "00:00:00")), ""
                    )
                )
            ) {

            }
        }
    }
}

@Preview
@Composable
fun LogsScreenDetailUnsuccessfulPreview() {
    BluModifyTheme {
        Surface {
            LogsScreenDetail(
                navController = rememberNavController(),
                id = 0,
                error = null,
                LogsDetailState.Loaded(
                    LogReportUiItem(
                        "0", "1.1.1999", false, arrayOf(BtItem("Device", "00:00:00")), "Stacktrace"
                    )
                )
            ) {

            }
        }
    }
}

