package com.tomasrepcik.blumodify.settings.advanced.shared.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

@Composable
fun DevicePickerComp(
    dataItems: List<BtItem>,
    action: DeviceAction,
    modifier: Modifier = Modifier,
    onClick: (BtItem) -> Unit,
) {
    Box(
        modifier = modifier
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
        BtItem("00-B0-D0-63-C2-26", "Long example of the device name to test"),
        BtItem("00-B0-D0-63-C2-26", "BtDevice"),
        BtItem("00-B0-D0-63-C2-26", "BtDevice"),
        BtItem("00-B0-D0-63-C2-26", "BtDevice")
    )


    BluModifyTheme {
        Surface {
            DevicePickerComp(devices, DeviceAction.ADD){}
        }
    }
}

