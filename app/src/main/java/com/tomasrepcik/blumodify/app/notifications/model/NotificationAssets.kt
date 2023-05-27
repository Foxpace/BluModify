package com.tomasrepcik.blumodify.app.notifications.model

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NotificationAssets(
    @StringRes val notificationTitle: Int,
    @StringRes val notificationContent: Int,
    @DrawableRes val notificationButtonIcon: Int?,
    @StringRes val notificationButtonText: Int?,
    val intent: Intent?
)