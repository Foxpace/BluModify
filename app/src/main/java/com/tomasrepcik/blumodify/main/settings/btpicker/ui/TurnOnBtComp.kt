package com.tomasrepcik.blumodify.main.settings.btpicker.ui

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.ui.components.AppButton
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme


@Composable
fun TurnOnBtComp(onBtOn: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            onBtOn()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.size(120.dp),
            painter = painterResource(id = R.drawable.ic_bt_inactive),
            contentDescription = stringResource(
                id =
                R.string.ic_bt_off
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.settings_bt_off),
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.weight(1f))
        AppButton(text = R.string.settings_bt_off_button) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            launcher.launch(enableBtIntent)
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@AllScreenPreview
@Composable
fun TurnOnBtCompPreview() {
    BluModifyTheme {
        Surface {
            TurnOnBtComp {}
        }
    }
}