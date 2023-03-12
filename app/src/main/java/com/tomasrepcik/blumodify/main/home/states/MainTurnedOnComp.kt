package com.tomasrepcik.blumodify.main.home.states

import androidx.compose.runtime.Composable
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.AppButton

@Composable
fun MainTurnedOnComp(onClick: () -> Unit) {
    AppButton(onClick = onClick, text = R.string.main_screen_turn_off)
}