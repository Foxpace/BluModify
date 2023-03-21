package com.tomasrepcik.blumodify.home.states

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tomasrepcik.blumodify.app.model.AppResult

@Composable
fun MainErrorComp(error: AppResult.Error<Boolean>, onClick: () -> Unit) {
    Text("Error")
}