package com.tomasrepcik.blumodify.app.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tomasrepcik.blumodify.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepo @Inject constructor(@ApplicationContext private val context: Context) :
    NotificationRepoTemplate {

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

    override suspend fun postNotificationToCancelBt() = withContext(Dispatchers.Main) {
        postNotification(
            context.resources.getString(R.string.app_name),
            context.resources.getString(R.string.notification_content)
        )
    }

    @SuppressLint("MissingPermission")
    override suspend fun postNotification(title: String, text: String) =
        withContext(Dispatchers.Main) {

            if (!isChannelInitialized.value) {
                initialize()
            }
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_app_square)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(context)) {
                if (isPermission()) {
                    notify(NOTIFICATION_ID, builder.build())
                }

            }
        }

    companion object {
        const val TAG = "NotificationRepo"
        const val CHANNEL_ID = "com.tomasrepcik.blumodify.notification.channel"
        const val NOTIFICATION_ID = 64684648
    }
}