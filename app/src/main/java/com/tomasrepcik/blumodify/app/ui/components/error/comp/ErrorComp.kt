package com.tomasrepcik.blumodify.app.ui.components.error.comp

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.ui.components.AppButton
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.advanced.devicelist.vm.DeviceListState


@Composable
fun <T> ErrorComp(
    @StringRes explanation: Int,
    @StringRes primaryText: Int? = null,
    @StringRes secondaryText: Int? = null,
    error: AppResult.Error<T>? = null,
    onPrimaryClick: (() -> Unit)?,
    onSecondaryClick: ((AppResult<T>?) -> Unit)? = null,
    ignoreDetails: Boolean = true,
    onDetails: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                painter = painterResource(id = R.drawable.ic_sad),
                contentDescription = stringResource(
                    id = R.string.ic_sad
                ),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = explanation),
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (error != null)
                Text(
                    text = error.message,
                    style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Start)
                )
        }

        Column {
            if (!ignoreDetails){
                AppButton(text = R.string.show_details, onClick = onDetails)
            }
            if (onPrimaryClick != null && primaryText != null) {
                Spacer(modifier = Modifier.height(16.dp))
                AppButton(text = primaryText, onClick = onPrimaryClick)
            }
            if (onPrimaryClick != null && onSecondaryClick != null) {
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (secondaryText != null && onSecondaryClick != null) {
                AppButton(text = secondaryText, onClick = { onSecondaryClick(error) })
            }

        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@AllScreenPreview
@Composable
fun ErrorCompPreview() {
    BluModifyTheme {
        Surface {
            ErrorComp<DeviceListState>(
                explanation = R.string.settings_no_tracked_device,
                primaryText = R.string.settings_bt_picker,
                secondaryText = R.string.back,
                onPrimaryClick = {},
                onSecondaryClick = {},
                onDetails = {},
            )
        }
    }
}