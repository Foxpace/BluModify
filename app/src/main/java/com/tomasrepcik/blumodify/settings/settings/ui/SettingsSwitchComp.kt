package com.tomasrepcik.blumodify.settings.settings.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SettingsSwitchComp(
    @DrawableRes icon: Int,
    @StringRes iconDesc: Int,
    @StringRes name: Int,
    state: StateFlow<Boolean>,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = onClick,
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(id = icon),
                        contentDescription = stringResource(id = iconDesc),
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = name),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.surfaceTint
                        ),
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.weight(1.0f))
                Switch(
                    checked = state.collectAsState().value,
                    onCheckedChange = { onClick() }
                )
            }
            Divider()
        }

    }
}