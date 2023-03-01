package com.tomasrepcik.blumodify.main.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tomasrepcik.blumodify.ui.components.AppBar

@Composable
fun SettingsScreen(drawerState: DrawerState) {
    Scaffold(
        topBar = { AppBar(drawerState = drawerState) }
    ) {
        Text("Settings", modifier = Modifier.padding(it))
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    SettingsScreen(drawerState)
}