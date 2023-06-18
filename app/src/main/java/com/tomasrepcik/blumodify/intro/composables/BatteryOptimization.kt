package com.tomasrepcik.blumodify.intro.composables

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tomasrepcik.blumodify.NavRoutes
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.AppButton
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.intro.IntroViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun BatteryOptimizationScreen(
    navController: NavController = rememberNavController(),
    viewModel: IntroViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                data ->
                Snackbar(
                    data,
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_battery_happy),
                contentDescription = stringResource(id = R.string.ic_battery_happy),
                contentScale = ContentScale.Fit,
                modifier = Modifier.weight(5f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                stringResource(id = R.string.battery_saving_title),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                stringResource(id = R.string.battery_saving_text),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))
            AppButton(
                modifier = Modifier.padding(bottom = 16.dp),
                text = R.string.battery_saving_activate_button
            ) {
                PowerManagement.tryToIgnoreBatteryOptimisations(
                    context, coroutineScope, snackbarHostState
                )
            }
            AppButton(
                modifier = Modifier.padding(bottom = 30.dp),
                text = R.string.start_app
            ) {
                viewModel.saveUserOnboarding()
                navController.navigate(NavRoutes.MainRoute.name) {
                    popUpTo(NavRoutes.IntroRoute.name)
                }
            }
        }
    }
}

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


@AllScreenPreview
@Composable
fun BatteryOptimizationPreview() {
    BluModifyTheme {
        BatteryOptimizationScreen()
    }
}

