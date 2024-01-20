package com.tomasrepcik.blumodify.e2e.settings

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
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DataStoreDI::class)
class MainSettingsTest : UiTest(TestConfig.AllPermissions) {

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true))

    @Test
    fun checkMainSettings() {

        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithoutAdvanced()
        }
    }

    @Test
    fun checkShowAdvancedSettings() {

        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithoutAdvanced()
            enableAdvanced()
            checkScreenContentWithAdvanced()
            enableAdvanced()
            checkScreenContentWithoutAdvanced()
        }
    }
}

@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DataStoreDI::class)
class MainSettingsAdvancedOnTest : UiTest(TestConfig.AllPermissions) {

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true, isAdvancedSettings = true))

    @Test
    fun checkMainSettings_advancedOn() {

        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithAdvanced()
        }
    }
}