package com.tomasrepcik.blumodify.app.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tomasrepcik.blumodify.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepo @Inject constructor(@ApplicationContext private val context: Context) :
    NotificationRepoTemplate {

    private var isChannelInitialized = false

    private fun initialize() {
        Log.i(TAG, "Initializing the notification channel")
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            CHANNEL_ID, context.getString(R.string.notification_channel_name), importance
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        Log.i(TAG, "Registering the notification repo to bluetooth controller")
        isChannelInitialized = true
    }

    override fun isPermission(): Boolean {
        Log.i(TAG, "Checking notification permission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                context, Manifest.permission_group.NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        Log.i(TAG, "Notification permission is not needed - lower than Android T")
        return true
    }

    override suspend fun cancelNotifications() {
        Log.i(TAG, "Shutting down any possible notification")
        with(NotificationManagerCompat.from(context)) {
            val notifications = this.activeNotifications
            notifications.forEach { notification ->
                if (notification.id == NOTIFICATION_ID) {
                    cancel(NOTIFICATION_ID)
                }
            }
        }
    }

    override suspend fun onBtChange(btIsOn: Boolean) {
        Log.i(TAG, "Bluetooth changed in notification repo")
        if (!isPermission()) {
            Log.w(TAG, "No notification permission is granted")
            return
        }

        if (btIsOn) {
            Log.i(TAG, "Bluetooth is still on - the notifications will persist")
            return
        }

        cancelNotifications()

    }

    @SuppressLint("MissingPermission")
    override suspend fun postNotification(
        @StringRes title: Int,
        @StringRes text: Int,
        intent: Intent,
        @DrawableRes buttonIcon: Int,
        @StringRes buttonText: Int
    ): Unit = withContext(Dispatchers.Main) {

        if (!isPermission()) {
            Log.w(TAG, "Missing permission for notification - unable to post notification")
            return@withContext
        }

        if (!isChannelInitialized) {
            Log.i(TAG, "Notification channel was not initialized - initializing now")
            initialize()
        }

        val titleString = context.getString(title)
        val textString = context.getString(text)
        val buttonString = context.getString(buttonText)

        Log.i(TAG, "Posting new notification")
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder =
            NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.ic_app_square)
                .setContentTitle(titleString).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle().bigText(textString))
                .setContentIntent(pendingIntent).addAction(
                    buttonIcon, buttonString, pendingIntent
                )

        with(NotificationManagerCompat.from(context)) {
            cancel(NOTIFICATION_ID)
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    companion object {
        const val TAG = "NotificationRepo"
        const val CHANNEL_ID = "com.tomasrepcik.blumodify.notification.channel"
        const val NOTIFICATION_ID = 64684648
    }
}