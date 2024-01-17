package com.tomasrepcik.blumodify.e2e.intro

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.app.storage.DataStoreDI
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.e2e.helpers.UiTest
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import com.tomasrepcik.blumodify.e2e.helpers.launchApp
import com.tomasrepcik.blumodify.e2e.robots.IntroRobot
import com.tomasrepcik.blumodify.e2e.robots.MainRobot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
@UninstallModules(DataStoreDI::class)
class IntroFlowTest : UiTest(TestConfig.AllPermissions) {

    override val hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> = generateSettingsMock()

    @Test
    fun passIntro() {
        launchApp<MainActivity>()

        with(IntroRobot(composeTestRule)) {
            clickWelcomeButton()
            clickEnergyButton()
            clickPrivacyButton()
            clickRecommendationButton()
            clickBatteryStartButton()
        }
        with(MainRobot(composeTestRule)) {
            checkMainScreen()
        }
    }
}