package com.tomasrepcik.blumodify.main.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar
import com.tomasrepcik.blumodify.bluetooth.model.BlumodifyState
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyViewModel
import com.tomasrepcik.blumodify.main.home.states.MainErrorComp
import com.tomasrepcik.blumodify.main.home.states.MainNothingToTrackComp
import com.tomasrepcik.blumodify.main.home.states.MainTurnedOffComp
import com.tomasrepcik.blumodify.main.home.states.MainTurnedOnComp
import com.tomasrepcik.blumodify.app.ui.components.loading.LoadingComp

@Composable
fun HomeScreen(
    navController: NavController,
    drawerState: DrawerState,
    vm: BluModifyViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        vm.onLaunch()
    }

    Scaffold(
        topBar = { AppBar(drawerState = drawerState) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            when (val bluModifyState = vm.blumodifyState.collectAsState().value) {
                BlumodifyState.Loading -> LoadingComp()
                is BlumodifyState.ErrorOccurred -> MainErrorComp(bluModifyState.error){}
                BlumodifyState.NothingToTrack -> MainNothingToTrackComp {

                }
                is BlumodifyState.TurnedOff -> MainTurnedOffComp {
                    vm.onButtonClicked()
                }
                is BlumodifyState.TurnedOn -> MainTurnedOnComp {
                    vm.onButtonClicked()
                }
            }
        }
    }
}