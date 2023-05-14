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
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.controller.BtObserver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepo @Inject constructor(
    @ApplicationContext private val context: Context,
    private val btControllerTemplate: BtControllerTemplate
) :
    NotificationRepoTemplate, BtObserver {

    private val _isChannelInitialized = MutableStateFlow(false)
    private var isChannelInitialized = _isChannelInitialized.asStateFlow()

    override fun initialize() {
        Log.i(TAG, "Initializing the notification channel")
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            importance
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        Log.i(TAG, "Registering the notification repo to bluetooth controller")
        btControllerTemplate.registerObserver(this)
        _isChannelInitialized.value = true
    }

    override fun isPermission(): Boolean {
        Log.i(TAG, "Checking notification permission")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission_group.NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        Log.i(TAG, "Notification permission is not needed - lower than Android T")
        return true
    }

    override suspend fun postNotificationToCancelBt() {
        Log.i(TAG, "Pushing notification for BT cancellation")
        withContext(Dispatchers.Main) {
            postNotification(
                context.resources.getString(R.string.app_name),
                context.resources.getString(R.string.notification_content)
            )
        }
    }

    override fun onBtChange(btIsOn: Boolean) {
        Log.i(TAG, "Bluetooth changed in notification repo")
        if (!btIsOn) {
            Log.i(TAG, "Shutting down any possible notification")
            with(NotificationManagerCompat.from(context)) {
                if (isPermission()) {
                    val notifications = this.activeNotifications
                    notifications.forEach {notification ->
                        if (notification.id == SHUT_DOWN_NOTIFICATION_ID) {
                            cancel(SHUT_DOWN_NOTIFICATION_ID)
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun postNotification(title: String, text: String): Unit =
        withContext(Dispatchers.Main) {

            if (!isChannelInitialized.value) {
                Log.i(TAG, "Notification channel was not initialized - initializing now")
                initialize()
            }

            Log.i(TAG, "Posting new notification")
            val intent = Intent(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_square)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setContentIntent(pendingIntent)
                .addAction(
                    R.drawable.ic_bt_black,
                    context.getString(R.string.main_screen_turn_off),
                    pendingIntent
                )

            with(NotificationManagerCompat.from(context)) {
                if (isPermission()) {
                    Log.i(TAG, "Got permission - posting now")
                    cancel(SHUT_DOWN_NOTIFICATION_ID)
                    notify(SHUT_DOWN_NOTIFICATION_ID, builder.build())
                }else {
                    Log.w(TAG, "Missing permission for notification - unable to post notification")
                }
            }
        }

    companion object {
        const val TAG = "NotificationRepo"
        const val CHANNEL_ID = "com.tomasrepcik.blumodify.notification.channel"
        const val SHUT_DOWN_NOTIFICATION_ID = 64684648
    }
}