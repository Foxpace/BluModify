package com.tomasrepcik.blumodify.e2e

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tomasrepcik.blumodify.e2e.helpers.UiTest
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import com.tomasrepcik.blumodify.e2e.robots.IntroRobot
import com.tomasrepcik.blumodify.e2e.robots.MainScreenRobot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class IntroFlowTest: UiTest(TestConfig.AllPermissions) {

    override fun setUp() {
        super.setUp()
        clearCache()
    }

    @Test
    fun passIntro() {
        with(IntroRobot(composeTestRule)) {
            clickWelcomeButton()
            clickEnergyButton()
            clickPrivacyButton()
            clickRecommendationButton()
            clickBatteryStartButton()
        }
        with(MainScreenRobot(composeTestRule)) {
            checkMainScreen()
        }
    }



}