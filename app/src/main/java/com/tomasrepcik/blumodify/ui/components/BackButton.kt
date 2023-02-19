package com.tomasrepcik.blumodify.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.ui.previews.AllPreviews
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

@Composable
fun BackButton(@StringRes iconDescription: Int, onClick: OnClickFunction) {
    IconButton(
        onClick = onClick, modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            stringResource(id = iconDescription),
            modifier = Modifier
                .size(32.dp)
                .padding(0.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }

}

@AllPreviews
@Composable
fun BackButtonPreview() {
    BluModifyTheme {
        BackButton(iconDescription = R.string.ic_arrow_back) {}
    }
}