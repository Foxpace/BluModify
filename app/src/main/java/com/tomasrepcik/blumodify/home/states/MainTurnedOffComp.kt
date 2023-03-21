package com.tomasrepcik.blumodify.home.states

import androidx.compose.runtime.Composable
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.AppButton

@Composable
fun MainTurnedOffComp(onClick: () -> Unit) {
    AppButton(onClick = onClick, text = R.string.main_screen_turn_on)
}