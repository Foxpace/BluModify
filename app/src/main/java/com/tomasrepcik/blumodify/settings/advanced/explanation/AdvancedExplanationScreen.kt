package com.tomasrepcik.blumodify.settings.advanced.explanation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.ui.components.BackButton
import com.tomasrepcik.blumodify.app.ui.components.appbar.AppBar

@Composable
fun AdvancedExplanationScreen(
    navController: NavHostController
) {
    Scaffold(topBar = {
        AppBar(title = R.string.settings_advanced_tracking_dialog_title, navigationIcon = {
            BackButton {
                navController.popBackStack()
            }
        })
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                stringResource(id = R.string.settings_advanced_tracking_dialog_text),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

