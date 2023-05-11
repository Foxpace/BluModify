package com.tomasrepcik.blumodify.home.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyState

@Composable
fun MainScreenWithAnimation(state: BluModifyState, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        MainRiveAnimation(
            modifier = Modifier.weight(1f),
            state = state
        )
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        when (state) {
            BluModifyState.TurnedOff -> MainButtonComp(
                title = R.string.main_screen_turn_on_title,
                text = R.string.main_screen_turn_on,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), onClick = onClick
            )

            BluModifyState.TurnedOn -> MainButtonComp(
                title = R.string.main_screen_turn_off_title,
                text = R.string.main_screen_turn_off,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), onClick = onClick
            )

            else -> MainButtonComp(
                title = R.string.main_screen_error,
                text = R.string.try_again,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), onClick = onClick
            )
        }

    }


}