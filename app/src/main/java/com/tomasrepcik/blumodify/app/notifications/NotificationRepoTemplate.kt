package com.tomasrepcik.blumodify.app.notifications

import com.tomasrepcik.blumodify.app.notifications.model.NotificationAssets
import com.tomasrepcik.blumodify.bluetooth.controller.BtObserver

interface NotificationRepoTemplate: BtObserver {

    fun isPermission(): Boolean
    suspend fun cancelNotifications()
    suspend fun postNotification(notificationAssets: NotificationAssets?)
}