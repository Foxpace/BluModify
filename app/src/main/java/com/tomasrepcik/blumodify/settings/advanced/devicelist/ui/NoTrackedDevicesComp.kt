package com.tomasrepcik.blumodify.settings.advanced.devicelist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.AppButton
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme


@Composable
fun NoTrackedDevices(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            painter = painterResource(id = R.drawable.ic_sad),
            contentDescription = stringResource(
                id =
                R.string.ic_sad
            ),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.settings_no_tracked_device),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.weight(1f))
        AppButton(text = R.string.settings_bt_picker, onClick = onClick)
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@AllScreenPreview
@Composable
fun NoDeviceCompPreview() {
    BluModifyTheme {
        Surface {
            NoTrackedDevices {

            }
        }
    }
}