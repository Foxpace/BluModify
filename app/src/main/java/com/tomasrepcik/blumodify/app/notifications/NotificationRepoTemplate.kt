package com.tomasrepcik.blumodify.app.notifications

import android.content.Context

interface NotificationRepoTemplate {

    fun initialize(context: Context)
    fun isPermission(context: Context): Boolean
    suspend fun postNotification(context: Context, title: String, text: String)
}