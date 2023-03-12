package com.tomasrepcik.blumodify.app.ui.previews

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DarkPreview

@Preview
@DarkPreview
annotation class AllPreviews
