package com.tomasrepcik.blumodify.e2e.intro

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.e2e.helpers.UiTest
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import com.tomasrepcik.blumodify.e2e.helpers.launchApp
import com.tomasrepcik.blumodify.e2e.robots.IntroRobot
import com.tomasrepcik.blumodify.e2e.robots.MainScreenRobot
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class IntroFlowTest : UiTest(TestConfig.AllPermissions) {

    override fun setUp() {
        super.setUp()
        clearCache()
    }

    @Test
    fun passIntro() {

        launchApp<MainActivity>(
            onBefore = {
                clearCache()
            },
        )

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

    @Test
    fun bypassingIntro_UserIsOnboarded() {
        launchApp<MainActivity>(
            onBefore = {
                runBlocking {
                    cache.storeOnboarding(true)
                }
            },
        )

        with(IntroRobot(composeTestRule)) {
            isNotOnboarding()
        }
        with(MainScreenRobot(composeTestRule)) {
            checkMainScreen()
        }
    }

}