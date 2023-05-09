package com.tomasrepcik.blumodify.home

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.tomasrepcik.blumodify.R

@Composable
fun MainRiveAnimation(serviceRunning: Boolean) {
    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.main_animation, null, false)
        val riveAnimation = view.findViewById<RiveAnimationView>(R.id.home_main_animation)
        riveAnimation.setBooleanState(
            MainAnimation.MACHINE_NAME,
            MainAnimation.SERVICE_STATE,
            serviceRunning
        )
        view // return the view
    }, update = { view ->
        val riveAnimation = view.findViewById<RiveAnimationView>(R.id.home_main_animation)
        riveAnimation.setBooleanState(
            MainAnimation.MACHINE_NAME,
            MainAnimation.SERVICE_STATE,
            serviceRunning
        )
    })
}

private object MainAnimation {
    const val MACHINE_NAME = "MainMachine"
    const val SERVICE_STATE = "Service on"
}