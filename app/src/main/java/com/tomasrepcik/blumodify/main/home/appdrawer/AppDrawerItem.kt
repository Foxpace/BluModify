package com.tomasrepcik.blumodify.main.home.appdrawer

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun AppDrawerItem(item: AppDrawerItemInfo, onClick: (options: DrawerOption) -> Unit) = Row() {
    TextButton(onClick = { onClick(item.drawerOption) }) {
        Text(
            text = stringResource(id = item.title),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}