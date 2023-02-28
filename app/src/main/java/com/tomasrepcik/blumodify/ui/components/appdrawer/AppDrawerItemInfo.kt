package com.tomasrepcik.blumodify.ui.components.appdrawer

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AppDrawerItemInfo(
    val drawerOption: DrawerOption,
    @StringRes val title: Int,
    @DrawableRes val drawableId: Int,
    @StringRes val descriptionId: Int
)