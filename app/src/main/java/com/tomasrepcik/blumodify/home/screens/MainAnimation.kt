package com.tomasrepcik.blumodify.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.Alignment
import app.rive.runtime.kotlin.core.Loop
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.bluetooth.viewmodel.BluModifyState

@Composable
fun MainRiveAnimation(state: BluModifyState, modifier: Modifier) = AndroidView(
    modifier = modifier,
    factory = { context ->
        RiveAnimationView(context).apply {
            setRiveResource(
                resId = R.raw.blumodify_animation,
                stateMachineName = MainAnimation.MACHINE_NAME,
                alignment = Alignment.CENTER,
                loop = Loop.AUTO
            )
            setBooleanState(
                MainAnimation.MACHINE_NAME,
                MainAnimation.SERVICE_STATE,
                when (state) {
                    BluModifyState.TurnedOn -> true
                    else -> false
                }
            )
        } // return the view
    }, update = { view ->
        view.setBooleanState(
            MainAnimation.MACHINE_NAME,
            MainAnimation.SERVICE_STATE,
            when (state) {
                BluModifyState.TurnedOn -> true
                else -> false
            }
        )
    })

private object MainAnimation {
    const val MACHINE_NAME = "MainMachine"
    const val SERVICE_STATE = "Service on"
}