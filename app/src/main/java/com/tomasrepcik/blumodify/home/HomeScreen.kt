package com.tomasrepcik.blumodify.home

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.error.ErrorScreen
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyEvent
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyState
import com.tomasrepcik.blumodify.home.permissions.AppPermissions
import com.tomasrepcik.blumodify.home.screens.MainScreenWithAnimation

@Composable
fun HomeScreen(drawerState: DrawerState, state: BluModifyState, onEvent: (BluModifyEvent) -> Unit) {

    LaunchedEffect(Unit) {
        onEvent(BluModifyEvent.OnLaunch)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions: Map<String, Boolean> ->
        if (permissions.all { it.value }) {
            onEvent(BluModifyEvent.OnPermissionGranted)
        } else {
            onEvent(BluModifyEvent.OnPermissionDenied)
        }
    }

    val context = LocalContext.current
    val onButtonClick = hasReturn@{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !AppPermissions.hasPermissions(context, AppPermissions.permissionsSdkT)
        ) {
            launcher.launch(AppPermissions.permissionsSdkT)
            return@hasReturn
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !AppPermissions.hasPermissions(context, AppPermissions.permissionsSdkS)
        ) {
            launcher.launch(AppPermissions.permissionsSdkS)
            return@hasReturn
        }

        onEvent(BluModifyEvent.OnMainButtonClickEvent)
    }

    Scaffold(topBar = { AppBar(drawerState = drawerState) }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                BluModifyState.Loading -> LoadingComp()
                is BluModifyState.ErrorOccurred -> ErrorScreen(
                    explanation = R.string.main_screen_error,
                    error = state.error,
                    onPrimaryClick = {
                        onEvent(BluModifyEvent.OnError)
                    })

                BluModifyState.MissingPermission -> ErrorScreen<BluModifyState>(
                    explanation = R.string.settings_bt_permission,
                    primaryText = R.string.settings_bt_permission_button,
                    onPrimaryClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            launcher.launch(AppPermissions.permissionsSdkT)
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            launcher.launch(AppPermissions.permissionsSdkS)
                        } else {
                            onEvent(BluModifyEvent.OnPermissionGranted)
                        }
                    },
                    secondaryText = R.string.open_settings,
                    onSecondaryClick = {
                        AppPermissions.openSettings(context)
                    }
                )

                else -> MainScreenWithAnimation(state = state, onClick = onButtonClick)
            }
        }
    }
}