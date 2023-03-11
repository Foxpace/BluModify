package com.tomasrepcik.blumodify.main.settings.btpicker.ui.states

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.ui.components.AppButton
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme


@Composable
fun NoDeviceComp() {
    val context = LocalContext.current
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
            painter = painterResource(id = R.drawable.ic_bt_no_device),
            contentDescription = stringResource(
                id =
                R.string.ic_bt_no_device
            ),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.settings_bt_no_device),
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.weight(1f))
        AppButton(text = R.string.settings_bt_picker) {
            val intentOpenBluetoothSettings = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            context.startActivity(intentOpenBluetoothSettings)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@AllScreenPreview
@Composable
fun NoDeviceCompPreview() {
    BluModifyTheme {
        Surface {
            NoDeviceComp()
        }
    }
}