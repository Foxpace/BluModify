package com.tomasrepcik.blumodify.app.ui.components.error.comp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.mail.MailSending
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.ui.components.AppButton
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListState


@Composable
fun <T> ErrorDetailComp(
    error: AppResult.Error<T>? = null,
    onBackButton: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ErrorTextLine(
                title = stringResource(id = R.string.error_screen_message_title),
                description = error?.message ?: stringResource(id = R.string.none)
            )
            ErrorTextLine(
                title = stringResource(id = R.string.error_screen_origin_title),
                description = error?.origin ?: stringResource(id = R.string.none)
            )
            ErrorTextLine(
                title = stringResource(id = R.string.error_screen_type_title),
                description = error?.errorCause?.name ?: stringResource(id = R.string.none)
            )
            ErrorTextLine(
                title = stringResource(id = R.string.error_screen_stacktrace_title),
                description = error?.stacktrace?.toString() ?: stringResource(id = R.string.none)
            )

        }
        if (error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(text = R.string.send_report) {
                MailSending.reportError(context, error)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = R.string.back, onClick = onBackButton)
    }
}

@Composable
private fun ErrorTextLine(title: String, description: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
    }

}

@AllScreenPreview
@Composable
fun ErrorDetailPreview() {
    BluModifyTheme {
        Surface {
            ErrorDetailComp<DeviceListState>(
                error = AppResult.Error(
                    "Error", "origin", ErrorCause.WORKER_NOT_FOUND, Error("Simulated error")
                )
            ) {

            }
        }
    }
}