package com.tomasrepcik.blumodify.intro.composables.battery

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope

sealed class BatteryOptimizationEvents {
    data object FinishOnboarding : BatteryOptimizationEvents()
    data class RemoveBatteryOptimization(
        val context: Context,
        val scope: CoroutineScope,
        val snackbarHostState: SnackbarHostState
    ) : BatteryOptimizationEvents()
}