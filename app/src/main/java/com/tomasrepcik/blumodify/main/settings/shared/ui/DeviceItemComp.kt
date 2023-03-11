package com.tomasrepcik.blumodify.main.settings.shared.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.main.settings.shared.model.BtItem
import com.tomasrepcik.blumodify.main.settings.shared.ui.DeviceAction.ADD
import com.tomasrepcik.blumodify.main.settings.shared.ui.DeviceAction.DELETE

@Composable
fun <T : BtItem> DeviceItemComp(device: T, action: DeviceAction, onClick: (T) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        onClick = { onClick(device) },
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
                    text = device.deviceName,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.surfaceTint
                    ),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = device.macAddress,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.surfaceTint
                    ),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .size(16.dp),
                painter = painterResource(
                    id = when (action) {
                        ADD -> R.drawable.ic_add
                        DELETE -> R.drawable.ic_minus
                    }
                ),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = stringResource(
                    id = when (action) {
                        ADD -> R.string.settings_bt_picker
                        DELETE -> R.string.ic_minus
                    },
                )
            )
        }

    }
}