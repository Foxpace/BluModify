package com.tomasrepcik.blumodify.main.settings

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.main.settings.contents.DevicesBottomSheet
import com.tomasrepcik.blumodify.main.settings.contents.SettingsScreenContent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(drawerState: DrawerState) {

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
    )

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetContent = {
            DevicesBottomSheet(modalSheetState = modalSheetState)
        },
        content = {
            SettingsScreenContent(drawerState = drawerState, modalSheetState = modalSheetState)
        }
    )
}