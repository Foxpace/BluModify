package com.tomasrepcik.blumodify.main.settings.ui.btpicker.states

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.bluetooth.controllers.bluetooth.BtController
import com.tomasrepcik.blumodify.main.settings.viewmodel.BtPickerViewModel
import com.tomasrepcik.blumodify.ui.components.AppButton
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun PermissionComp(vm: BtPickerViewModel = hiltViewModel()) {

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted){
            vm.onBtPermissionGranted()
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
            painter = painterResource(id = R.drawable.ic_bt_sad), contentDescription = stringResource(
                id =
                R.string.ic_bt_permission
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.settings_bt_permission),
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
        )
        Spacer(modifier = Modifier.weight(1f))
        AppButton(text = R.string.settings_bt_permission_button) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                launcher.launch(Manifest.permission_group.NEARBY_DEVICES)
            } else {
                vm.onBtPermissionGranted()
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}



@AllScreenPreview
@Composable
fun PermissionCompPreview() {
    val context = LocalContext.current
    BluModifyTheme {
        PermissionComp(vm = BtPickerViewModel(btController = BtController(context)))
    }
}