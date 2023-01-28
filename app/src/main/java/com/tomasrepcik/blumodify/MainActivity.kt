package com.tomasrepcik.blumodify

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tomasrepcik.blumodify.intro.IntroViewModel
import com.tomasrepcik.blumodify.intro.model.UserOnboarded
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val viewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                return@setKeepOnScreenCondition viewModel.userOnboarded.value == UserOnboarded.Loading
            }
            setOnExitAnimationListener{ splashScreen ->
                val slideUp = ObjectAnimator.ofFloat(
                        splashScreen.view,
                View.TRANSLATION_Y,
                0f,
                splashScreen.view.height.toFloat()
                )
                slideUp.apply {
                    interpolator = AnticipateInterpolator()
                    duration = 500L
                    doOnEnd { splashScreen.remove() }
                    start()
                }
            }
        }
        setContent {
            MainCompose()
        }
    }

}

