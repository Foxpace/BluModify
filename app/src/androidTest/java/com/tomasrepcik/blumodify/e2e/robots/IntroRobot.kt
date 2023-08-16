package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.intro.IntroTestTags

class IntroRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun clickWelcomeButton() = clickButton(IntroTestTags.INTRO_WELCOME_SCREEN_NEXT_BUTTON)
    fun clickEnergyButton() = clickButton(IntroTestTags.INTRO_MOTIVATION_ENERGY_SCREEN_NEXT_BUTTON)
    fun clickPrivacyButton() =
        clickButton(IntroTestTags.INTRO_MOTIVATION_PRIVACY_SCREEN_NEXT_BUTTON)

    fun clickRecommendationButton() =
        clickButton(IntroTestTags.INTRO_RECOMMENDATION_SCREEN_NEXT_BUTTON)

    fun clickBatteryStartButton() =
        clickButton(IntroTestTags.INTRO_BATTERY_OPTIMISATION_SCREEN_START_BUTTON)

    fun isNotOnboarding() = isNotAnyTestTag(*IntroTestTags.INTRO_SCREENS)


}