package com.tomasrepcik.blumodify.ui.previews

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(device = "id:pixel_5")
@Preview(device = "id:pixel")
annotation class BrightScreens

@Preview(device = "id:pixel_5", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(device = "id:pixel", uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DarkScreens
