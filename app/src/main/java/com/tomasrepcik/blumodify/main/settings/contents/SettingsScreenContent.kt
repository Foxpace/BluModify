@file:OptIn(ExperimentalMaterialApi::class)

package com.tomasrepcik.blumodify.main.settings.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.ui.components.appbar.AppBarAction
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme
import kotlinx.coroutines.launch

@Composable
fun SettingsScreenContent(drawerState: DrawerState, modalSheetState: ModalBottomSheetState) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            AppBar(
                drawerState = drawerState,
                title = R.string.settings_tracked_devices,
                appBarActions = arrayListOf(
                    AppBarAction(
                        R.drawable.ic_add,
                        R.string.settings_tracked_button_description
                    ) {
                        coroutineScope.launch {
                            modalSheetState.show()
                        }
                    }
                )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            LazyColumn {

            }
        }
    }
}

@AllScreenPreview
@Composable
fun SettingsScreenPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    BluModifyTheme {
        SettingsScreenContent(drawerState, modalSheetState)
    }
}