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
import com.tomasrepcik.blumodify.e2e.robots.MainRobot
import com.tomasrepcik.blumodify.e2e.robots.NavigationRobot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DataStoreDI::class)
class MainScreenTest : UiTest(TestConfig.AllPermissions) {

    override val hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true))

    @Test
    fun checkIfJobIsOff() {

        launchApp<MainActivity>()

        with(MainRobot(composeTestRule)) {
            checkIdling()
        }
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
    fun checkDrawer() {

        launchApp<MainActivity>()

        with(MainRobot(composeTestRule)) {
            checkIdling()
        }

        with(NavigationRobot(composeTestRule)) {
            openAndCheckDrawer()
        }
    }

    @Test
    fun navigateThroughDrawer() {

        launchApp<MainActivity>()

        with(MainRobot(composeTestRule)) {
            checkIdling()
        }

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
            openAboutScreenViaDrawer()
            openMainScreenViaDrawer()
        }
    }

}