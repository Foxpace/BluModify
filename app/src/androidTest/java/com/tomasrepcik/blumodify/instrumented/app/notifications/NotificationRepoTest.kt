package com.tomasrepcik.blumodify.instrumented.app.notifications

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.tomasrepcik.blumodify.R
import com.tomasrepcik.blumodify.app.notifications.NotificationRepo
import com.tomasrepcik.blumodify.app.notifications.NotificationRepoTemplate
import com.tomasrepcik.blumodify.app.notifications.model.NotificationAssets
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NotificationRepoTest {

    private lateinit var notificationRepoTemplate: NotificationRepoTemplate

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val notificationPermissions: GrantPermissionRule =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) GrantPermissionRule.grant(
            Manifest.permission.POST_NOTIFICATIONS
        ) else GrantPermissionRule.grant()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        notificationRepoTemplate = NotificationRepo(context)
    }

    @After
    fun tearDown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
    }

    @Test
    fun showNotification() = runBlocking {
        // ACTION
        notificationRepoTemplate.postNotification(
            NotificationAssets(
                R.string.app_name,
                R.string.notification_content,
                R.drawable.ic_bt_black,
                R.string.main_screen_turn_off,
                Intent(Settings.ACTION_BLUETOOTH_SETTINGS),
            )
        )

        // CHECK
        val context = ApplicationProvider.getApplicationContext<Context>()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        composeTestRule.waitUntil { manager.activeNotifications.isNotEmpty() }

        manager.activeNotifications.first().let {
            assertEquals(it.id, NotificationRepo.NOTIFICATION_ID)
        }

    }

    @Test
    fun cancelNotification() = runBlocking {
        // ACTION
        notificationRepoTemplate.postNotification(
            NotificationAssets(
                R.string.app_name,
                R.string.notification_content,
                R.drawable.ic_bt_black,
                R.string.main_screen_turn_off,
                Intent(Settings.ACTION_BLUETOOTH_SETTINGS),
            )
        )

        // wait for notification
        val context = ApplicationProvider.getApplicationContext<Context>()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        composeTestRule.waitUntil { manager.activeNotifications.isNotEmpty() }

        // cancel notification
        notificationRepoTemplate.cancelNotifications()
        composeTestRule.waitUntil { manager.activeNotifications.isEmpty() }

        // CHECK
        assertTrue(manager.activeNotifications.isEmpty())


    }
}