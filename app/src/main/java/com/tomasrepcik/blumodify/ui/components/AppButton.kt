package com.tomasrepcik.blumodify.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.ui.previews.AllPreviews
import com.tomasrepcik.blumodify.ui.theme.BluModifyTheme

typealias OnClickFunction = () -> Unit

@Composable
fun AppButton(@StringRes text: Int, onClick: OnClickFunction) {
    Button(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        onClick = onClick
    ) {
        Text(
            stringResource(id = text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@AllPreviews
@Composable
fun AppButtonPreview() {
    BluModifyTheme {
        AppButton(text = R.string.welcome_text) {}
    }
}