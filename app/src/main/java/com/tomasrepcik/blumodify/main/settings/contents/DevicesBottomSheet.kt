@file:OptIn(ExperimentalMaterialApi::class)

package com.tomasrepcik.blumodify.main.settings.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.tomasrepcik.blumodify.main.settings.viewmodel.SettingsViewModel
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme
import kotlinx.coroutines.launch

@Composable
fun DevicesBottomSheet(
    vm: SettingsViewModel = hiltViewModel(),
    modalSheetState: ModalBottomSheetState
) {
    val coroutineScope = rememberCoroutineScope()
    BluModifyTheme {
        Surface {
            Column() {
                IconButton(onClick = {
                    coroutineScope.launch {
                        modalSheetState.hide()
                    }
                }) {
                    Icon(Icons.Rounded.Close, contentDescription = "close button")
                }

            }
        }
    }
}

@AllScreenPreview
@Composable
fun DevicesBottomSheetPreview() {
    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    BluModifyTheme {
        DevicesBottomSheet(modalSheetState = modalSheetState)
    }
}