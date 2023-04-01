package com.tomasrepcik.blumodify.settings.logs.list.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.settings.logs.list.LogReportUiListItem

@Composable
fun LogReportCompItem(log: LogReportUiListItem, onClick: (id: Int) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        onClick = {
            onClick(log.id)
        },
        shape = RoundedCornerShape(20),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = log.time,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.surfaceTint
                    ),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = if (log.isSuccess) R.string.success else R.string.failure),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.surfaceTint
                    ),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        id = R.string.settings_logs_devices_count,
                        formatArgs = arrayOf(log.connectedDevices)
                    ),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.surfaceTint
                    ),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(
                Icons.Rounded.KeyboardArrowRight,
                modifier = Modifier.padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = stringResource(id = R.string.ic_arrow_forward)
            )
        }
    }
}