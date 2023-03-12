package com.tomasrepcik.blumodify.main.settings.shared.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tomasrepcik.blumodify.main.settings.shared.model.BtDeviceToPick
import com.tomasrepcik.blumodify.main.settings.shared.model.BtItem
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme

@Composable
fun <T: BtItem> DevicePickerComp(
    dataItems: List<T>,
    action: DeviceAction,
    onClick: (T) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(dataItems) {
                DeviceItemComp(it, action){ picked ->
                    onClick(picked)
                }
            }
        }
    }
}



enum class DeviceAction {
    ADD,
    DELETE
}



@AllScreenPreview
@Composable
fun PickDeviceCompPreview() {

    val devices = arrayListOf(
        BtDeviceToPick("00-B0-D0-63-C2-26", "Long example of the device name to test"),
        BtDeviceToPick("00-B0-D0-63-C2-26", "BtDevice"),
        BtDeviceToPick("00-B0-D0-63-C2-26", "BtDevice"),
        BtDeviceToPick("00-B0-D0-63-C2-26", "BtDevice")
    )


    BluModifyTheme {
        Surface {
            DevicePickerComp(devices, DeviceAction.ADD){}
        }
    }
}

