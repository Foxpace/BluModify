package com.tomasrepcik.blumodify.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp
import com.tomasrepcik.blumodify.bluetooth.model.BlumodifyState
import com.tomasrepcik.blumodify.home.events.HomeScreenEvent
import com.tomasrepcik.blumodify.home.screens.MainErrorComp
import com.tomasrepcik.blumodify.home.screens.MainTurnedOffComp
import com.tomasrepcik.blumodify.home.screens.MainTurnedOnComp

@Composable
fun HomeScreen(drawerState: DrawerState, state: BlumodifyState, onEvent:(HomeScreenEvent) -> Unit) {

    LaunchedEffect(Unit) {
        onEvent(HomeScreenEvent.OnLaunch)
    }

    Scaffold(topBar = { AppBar(drawerState = drawerState) }) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.weight(1f))
                when (state) {
                    BlumodifyState.Loading -> LoadingComp()
                    is BlumodifyState.ErrorOccurred -> MainErrorComp(state.error) {}
                    is BlumodifyState.TurnedOff -> MainTurnedOffComp {
                        onEvent(HomeScreenEvent.OnMainButtonClickEvent)
                    }

                    is BlumodifyState.TurnedOn -> MainTurnedOnComp {
                        onEvent(HomeScreenEvent.OnMainButtonClickEvent)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}