package com.tomasrepcik.blumodify.app.ui.components.error.comp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.ui.components.AppButton
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListState


@Composable
fun <T> ErrorDetailComp(
    appResult: AppResult.Error<T>? = null,
    onDetail: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(id = R.string.error_screen_message_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = appResult?.message ?: stringResource(id = R.string.none),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = R.string.error_screen_type_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = appResult?.errorCause?.name ?: stringResource(id = R.string.none),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = stringResource(id = R.string.error_screen_stacktrace_title),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = appResult?.error?.toString() ?: stringResource(id = R.string.none),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        AppButton(text = R.string.back, onClick = onDetail)
    }
}

@AllScreenPreview
@Composable
fun ErrorDetailPreview() {
    BluModifyTheme {
        Surface {
            ErrorDetailComp<DeviceListState>(
                appResult = AppResult.Error(
                    "Error", ErrorCause.WORKER_NOT_FOUND, Error("Simulated error")
                )
            ) {

            }
        }
    }
}