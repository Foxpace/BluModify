@file:OptIn(ExperimentalMaterial3Api::class)

package com.tomasrepcik.blumodify.main.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun AboutScreen(drawerState: DrawerState) {
    Scaffold(
        topBar = { AppBar(drawerState = drawerState, title = R.string.about_screen_title) }
    ) {
        Column(modifier = Modifier.padding(it).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_app_empty),
                contentDescription = stringResource(
                    id = R.string.app_name
                )
            )
        }
    }
}

@AllScreenPreview
@Composable
fun AboutScreenPreview() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    BluModifyTheme() {
        AboutScreen(drawerState)
    }
}