package com.tomasrepcik.blumodify.app.notifications

interface NotificationRepoTemplate {

    fun initialize()
    fun isPermission(): Boolean
    suspend fun postNotificationToCancelBt()
    suspend fun postNotification(title: String, text: String)
}