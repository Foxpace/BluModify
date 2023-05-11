package com.tomasrepcik.blumodify.home

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.error.comp.ErrorComp
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyEvent
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyState
import com.tomasrepcik.blumodify.home.screens.MainScreenWithAnimation

@Composable
fun HomeScreen(drawerState: DrawerState, state: BluModifyState, onEvent: (BluModifyEvent) -> Unit) {

    LaunchedEffect(Unit) {
        onEvent(BluModifyEvent.OnLaunch)
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onEvent(BluModifyEvent.OnPermissionGranted)
        } else {
            onEvent(BluModifyEvent.OnPermissionDenied)
        }
    }

    val context = LocalContext.current
    val onButtonClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(
                context, Manifest.permission_group.NEARBY_DEVICES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(Manifest.permission_group.NEARBY_DEVICES)
        } else {
            onEvent(BluModifyEvent.OnMainButtonClickEvent)
        }
    }

    Scaffold(topBar = { AppBar(drawerState = drawerState) }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                BluModifyState.Loading -> LoadingComp()
                is BluModifyState.ErrorOccurred -> ErrorComp(explanation = R.string.main_screen_error,
                    appResult = state.error,
                    onClick = {
                        onEvent(BluModifyEvent.OnError)
                    })

                BluModifyState.MissingPermission -> ErrorComp<BluModifyState>(explanation = R.string.settings_bt_permission,
                    buttonText = R.string.settings_bt_permission_button,
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            launcher.launch(Manifest.permission_group.NEARBY_DEVICES)
                        } else {
                            onEvent(BluModifyEvent.OnPermissionGranted)
                        }
                    })
                else -> MainScreenWithAnimation(state = state, onClick = onButtonClick)
            }
        }
    }
}