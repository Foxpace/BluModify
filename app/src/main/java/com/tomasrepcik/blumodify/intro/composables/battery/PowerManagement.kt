package com.tomasrepcik.blumodify.intro.composables.battery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import com.tomasrepcik.blumodify.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object PowerManagement {

    /**
     * request to add app to whitelist
     */
    @SuppressLint("BatteryLife")
    fun tryToIgnoreBatteryOptimisations(
        context: Context, coroutineScope: CoroutineScope, snackbarState: SnackbarHostState
    ) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager? ?: return

        if (pm.isIgnoringBatteryOptimizations(context.packageName)) {
            coroutineScope.launch {
                snackbarState.showSnackbar(context.getString(R.string.battery_saving_already_added))
            }
        }

        val intent = Intent()
        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        intent.data = Uri.parse("package:" + context.packageName)

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            coroutineScope.launch {
                snackbarState.showSnackbar(context.getString(R.string.battery_saving_error))
            }
            Log.e("BatteryOptimisation", "Battery optimisation error", e)
        }
    }

}