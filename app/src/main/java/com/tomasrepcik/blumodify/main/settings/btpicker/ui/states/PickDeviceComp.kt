package com.tomasrepcik.blumodify.main.settings.btpicker.ui.states

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.tomasrepcik.blumodify.main.settings.btpicker.model.BtDeviceToPick
import com.tomasrepcik.blumodify.main.settings.btpicker.model.TrackedDevicesState
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun PickDeviceComp(
    state: TrackedDevicesState.DevicesToAdd,
    onDevicePicked: (BtDeviceToPick) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(state.devices) {
                DeviceItem(device = it, onClick = onDevicePicked)
            }
        }
    }
}

@Composable
private fun DeviceItem(device: BtDeviceToPick, onClick: (BtDeviceToPick) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp),
        onClick = {onClick(device)},
        shape = RoundedCornerShape(20),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text(
                    text = device.name,
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
                painter = painterResource(id = R.drawable.ic_add),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = stringResource(
                id = R.string.settings_bt_picker
            ))
        }

    }
}

@AllScreenPreview
@Composable
fun PickDeviceCompPreview() {
    val state = TrackedDevicesState.DevicesToAdd(
        arrayListOf(
            BtDeviceToPick("00-B0-D0-63-C2-26", "Long example of the device name to test"),
            BtDeviceToPick("00-B0-D0-63-C2-26", "BtDevice"),
            BtDeviceToPick("00-B0-D0-63-C2-26", "BtDevice"),
            BtDeviceToPick("00-B0-D0-63-C2-26", "BtDevice")
        )
    )

    BluModifyTheme {
        Surface {
            PickDeviceComp(state) {
            }
        }
    }
}