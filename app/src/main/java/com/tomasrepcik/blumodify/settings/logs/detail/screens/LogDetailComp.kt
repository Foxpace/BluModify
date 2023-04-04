package com.tomasrepcik.blumodify.settings.logs.detail.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem

@Composable
fun LogDetailComp(log: LogReportUiItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        LogItem(title = R.string.settings_logs_time_title, value = log.time)
        LogItem(
            title = R.string.settings_logs_success_title,
            value = stringResource(id = if (log.isSuccess) R.string.success else R.string.failure)
        )
        if (log.connectedDevices.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.settings_logs_devices_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
            )
            for (device in log.connectedDevices){
                Text(
                    text = device.deviceName,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = device.macAddress,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        } else {
            LogItem(
                title = R.string.settings_logs_devices_count_detail,
                value = stringResource(id = R.string.zero)
            )
        }
        if (log.stackTrace.isNotBlank()){
            LogItem(title = R.string.settings_logs_error_title, value = log.stackTrace)
        }
    }
}

@Composable
private fun LogItem(@StringRes title: Int, value: String) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = value,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Start,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@AllScreenPreview
@Composable
fun LogsListPreview() {
    BluModifyTheme {
        LogDetailComp(
            log = LogReportUiItem(
                "45547568865", "1.1.1990 22:45", true, arrayListOf(
                    BtItem("Device name", "00:00:00:00:00:00"),
                    BtItem("Device name", "00:00:00:00:00:00")
                ), "Stacktrace"
            )
        )
    }
}