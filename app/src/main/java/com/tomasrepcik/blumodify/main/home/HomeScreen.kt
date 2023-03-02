package com.tomasrepcik.blumodify.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyViewModel
import com.tomasrepcik.blumodify.ui.components.AppBar

@Composable
fun HomeScreen(
    drawerState: DrawerState,
    vm: BluModifyViewModel = hiltViewModel()

) {
    Scaffold(
        topBar = { AppBar(drawerState = drawerState) }
    ) { paddingValues ->
        Surface {
            Column(modifier = Modifier.padding(paddingValues)) {
                val isRunning = vm.isRunning.collectAsState()
                TextButton(onClick = { vm.toggleBt() }) {
                    Text("Turn ${if (isRunning.value) "Off" else "On"}")
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    HomeScreen(drawerState = drawerState)
}