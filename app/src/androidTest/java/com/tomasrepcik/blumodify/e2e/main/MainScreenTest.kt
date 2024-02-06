package com.tomasrepcik.blumodify.e2e.main

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.app.storage.DataStoreDI
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.e2e.helpers.UiTest
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import com.tomasrepcik.blumodify.e2e.helpers.launchApp
import com.tomasrepcik.blumodify.e2e.robots.AboutRobot
import com.tomasrepcik.blumodify.e2e.robots.MainRobot
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
class MainScreenTest : UiTest(TestConfig.AllPermissions) {

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true))

    @Test
    fun checkIfJobIsOff() {

        launchApp<MainActivity>()
        MainRobot(composeTestRule).checkIdling()

    }

    @Test
    fun checkIfJobIsOff_TurnItOn() {

        launchApp<MainActivity>()

        with(MainRobot(composeTestRule)) {
            checkIdling()
            clickTurnOnButton()
            checkWorking()
        }
    }

    @Test
    fun checkIfJobIsOff_TurnItOnAndOff() {

        launchApp<MainActivity>()

        with(MainRobot(composeTestRule)) {
            checkIdling()
            clickTurnOnButton()
            checkWorking()
            clickTurnOffButton()
            checkIdling()

        }
    }

    @Test
    fun checkAppDrawer() {

        launchApp<MainActivity>()

        MainRobot(composeTestRule).checkIdling()
        NavigationRobot(composeTestRule).openAndCheckDrawer()

    }

    @Test
    fun navigateThroughAppDrawerAllScreens() {

        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {

            MainRobot(composeTestRule).checkIdling()
            openSettingsScreenViaDrawer()
            MainSettingsRobot(composeTestRule).checkScreenContentWithoutAdvancedTracking()
            openAboutScreenViaDrawer()
            AboutRobot(composeTestRule).checkAboutScreen()
            openMainScreenViaDrawer()
            MainRobot(composeTestRule).checkMainScreen()

        }
    }

}