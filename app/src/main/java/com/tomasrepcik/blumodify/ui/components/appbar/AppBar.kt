package com.tomasrepcik.blumodify.ui.components.appbar

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    drawerState: DrawerState,
    @StringRes title: Int? = null,
    appBarActions: List<AppBarAction>? = null
) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        title = {
            title?.let {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        actions = {
            appBarActions?.let {
                for (appBarAction in it) {
                    AppBarAction(appBarAction)
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = stringResource(id = R.string.drawer_menu_description)
                )
            }
        },
    )
}

@Composable
fun AppBarAction(appBarAction: AppBarAction) {
    IconButton(onClick = appBarAction.onClick) {
        Icon(
            painter = painterResource(id = appBarAction.icon),
            modifier = Modifier.size(24.dp),
            contentDescription = stringResource(id = appBarAction.description)
        )
    }
}