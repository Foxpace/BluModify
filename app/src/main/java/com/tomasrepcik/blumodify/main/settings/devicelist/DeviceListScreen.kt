package com.tomasrepcik.blumodify.main.settings.devicelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.main.settings.SettingsNavOption
import com.tomasrepcik.blumodify.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.ui.components.appbar.AppBarAction
import com.tomasrepcik.blumodify.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun DeviceListScreen(
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            AppBar(
                title = R.string.settings_tracked_devices,
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            painterResource(id = R.drawable.ic_back),
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = stringResource(id = R.string.ic_arrow_back)
                        )
                    }
                },
                appBarActions = arrayListOf(
                    AppBarAction(
                        R.drawable.ic_add,
                        R.string.settings_bt_picker
                    ) {
                        navController.navigate(SettingsNavOption.SettingsBtPicker.name)
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
fun DeviceListScreenPreview() {
    val navigator = rememberNavController()
    BluModifyTheme {
        DeviceListScreen(navigator)
    }
}