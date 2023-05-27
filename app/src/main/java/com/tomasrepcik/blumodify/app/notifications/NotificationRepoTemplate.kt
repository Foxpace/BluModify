package com.tomasrepcik.blumodify.app.notifications

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tomasrepcik.blumodify.bluetooth.controller.BtObserver

interface NotificationRepoTemplate: BtObserver {

    fun isPermission(): Boolean

    suspend fun cancelNotifications()
    suspend fun postNotification(
        @StringRes title: Int,
        @StringRes text: Int,
        intent: Intent,
        @DrawableRes buttonIcon: Int,
        @StringRes buttonText: Int
    )
}