package com.tomasrepcik.blumodify.app.ui.components.error

import androidx.annotation.StringRes
import androidx.compose.runtime.*
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.ui.components.error.comp.ErrorComp
import com.tomasrepcik.blumodify.app.ui.components.error.comp.ErrorDetailComp


@Composable
fun <T> ErrorScreen(
    @StringRes explanation: Int,
    @StringRes buttonText: Int? = null,
    appResult: AppResult.Error<T>? = null,
    onClick: (() -> Unit)? = null
) {

    var showDetail by remember {
        mutableStateOf(false)
    }

    if (showDetail){
        ErrorDetailComp(appResult = appResult){
            showDetail = showDetail.not()
        }
    } else {
        ErrorComp<T>(explanation = explanation, buttonText = buttonText, onClick = onClick){
            showDetail = showDetail.not()
        }
    }
}