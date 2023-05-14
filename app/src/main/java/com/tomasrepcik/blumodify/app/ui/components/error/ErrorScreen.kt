package com.tomasrepcik.blumodify.app.ui.components.error

import androidx.annotation.StringRes
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.ui.components.error.comp.ErrorComp
import com.tomasrepcik.blumodify.app.ui.components.error.comp.ErrorDetailComp
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme


@Composable
fun <T> ErrorScreen(
    @StringRes explanation: Int,
    @StringRes primaryText: Int? = null,
    @StringRes secondaryText: Int? = null,
    error: AppResult.Error<T>? = null,
    onPrimaryClick: (() -> Unit)?,
    ignoreDetails: Boolean = true,
    onSecondaryClick: ((AppResult<T>?) -> Unit)? = null
) {

    var showDetail by remember {
        mutableStateOf(false)
    }

    if (showDetail) {
        ErrorDetailComp(error = error) {
            showDetail = showDetail.not()
        }
    } else {
        ErrorComp(
            explanation = explanation,
            primaryText = primaryText,
            onPrimaryClick = onPrimaryClick,
            onSecondaryClick = onSecondaryClick,
            secondaryText = secondaryText,
            ignoreDetails = ignoreDetails
        ) {
            showDetail = showDetail.not()
        }
    }
}

@AllScreenPreview
@Composable
fun ErrorScreenPreview() {
    BluModifyTheme() {
        Surface {
            ErrorScreen(
                explanation = R.string.main_screen_error,
                ignoreDetails = false,
                primaryText = R.string.back,
                onPrimaryClick = { },
                error = AppResult.Error<ErrorCause>(
                    "error",
                    "test",
                    ErrorCause.MISSING_SETTINGS,
                    "Stacktrace"
                )
            )
        }
    }
}