package com.tomasrepcik.blumodify.main.settings.btpicker.ui.states

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.main.settings.btpicker.model.BtDeviceToPick
import com.tomasrepcik.blumodify.main.settings.btpicker.model.TrackedDevicePickerState
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun PickDeviceComp(
    state: TrackedDevicePickerState.DevicesToAdd,
    onDevicePicked: (BtDeviceToPick) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn() {
            items(state.devices) {
                Text(it.name)
            }
        }
    }
}

@AllScreenPreview
@Composable
fun PickDeviceCompPreview() {
    val state = TrackedDevicePickerState.DevicesToAdd(arrayListOf(
        BtDeviceToPick("", "BtDevice"),
        BtDeviceToPick("", "BtDevice"),
        BtDeviceToPick("", "BtDevice"),
        BtDeviceToPick("", "BtDevice")
    ))

    BluModifyTheme {
        PickDeviceComp(state){
        }
    }
}