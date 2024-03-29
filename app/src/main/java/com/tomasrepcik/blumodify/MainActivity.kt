package com.tomasrepcik.blumodify

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.tomasrepcik.blumodify.intro.IntroViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                return@setKeepOnScreenCondition viewModel.isLoading.value
            }
            setOnExitAnimationListener { splashScreen ->
                ObjectAnimator.ofFloat(
                    splashScreen.view, View.TRANSLATION_Y, 0f, splashScreen.view.height.toFloat()
                ).apply {
                    interpolator = DecelerateInterpolator()
                    duration = 500L
                    doOnEnd { splashScreen.remove() }
                    start()
                }
            }
        }
        setContent {
            val introViewModel: IntroViewModel = hiltViewModel()
            MainCompose(isOnboarded = introViewModel.isOnboarded.collectAsState().value)
        }
    }
}

