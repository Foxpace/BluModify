package com.tomasrepcik.blumodify.e2e.settings.logs

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.app.storage.DataStoreDI
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.e2e.helpers.UiTest
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import com.tomasrepcik.blumodify.e2e.helpers.launchApp
import com.tomasrepcik.blumodify.e2e.robots.NavigationRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.MainSettingsRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.logs.LogsRobot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DataStoreDI::class)
class EmptyLogsTest : UiTest(TestConfig.AllPermissions) {

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true))

    @Test
    fun checkEmptyLogsTest() {

        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithoutAdvanced()
            openLogs()
        }

        with(LogsRobot(composeTestRule)) {
            val emptyLogsErrorMessage = "No logs are in database. Use the app for a while and some logs will be recorded."
            checkEmptyLogs(emptyLogsErrorMessage)
            goBackError()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithoutAdvanced()
        }
    }
}