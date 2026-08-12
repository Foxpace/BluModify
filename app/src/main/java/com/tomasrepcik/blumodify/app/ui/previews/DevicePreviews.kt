package com.tomasrepcik.blumodify.app.ui.previews

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(device = "id:pixel_5", name = "Pixel 5")
@Preview(device = "id:pixel", name = "Pixel")
annotation class BrightScreens

@Preview(device = "id:pixel_5", uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Pixel 5 - Dark")
@Preview(device = "id:pixel", uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Pixel - Dark")
annotation class DarkScreens

@BrightScreens
@DarkScreens
@Preview(name = "Compact landscape", widthDp = 840, heightDp = 480)
@Preview(name = "Expanded tablet", widthDp = 1280, heightDp = 800)
annotation class AllScreenPreview

@Preview(device = "id:pixel_5", name = "Pixel 5")
@Preview(device = "id:pixel_5", uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Pixel 5 - Dark")
annotation class BlackAndBright
