package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.intro.IntroTestTags

class IntroRobot(composeRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) :
    Robot(composeRule) {

    fun clickWelcomeButton() = clickButton(IntroTestTags.INTRO_WELCOME_SCREEN_NEXT_BUTTON)
    fun clickEnergyButton() = clickButton(IntroTestTags.INTRO_MOTIVATION_ENERGY_SCREEN_NEXT_BUTTON)
    fun clickPrivacyButton() =
        clickButton(IntroTestTags.INTRO_MOTIVATION_PRIVACY_SCREEN_NEXT_BUTTON)

    fun clickRecommendationButton() =
        clickButton(IntroTestTags.INTRO_RECOMMENDATION_SCREEN_NEXT_BUTTON)

    fun clickBatteryStartButton() =
        clickButton(IntroTestTags.INTRO_BATTERY_OPTIMISATION_SCREEN_START_BUTTON)


}