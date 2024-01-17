package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.intro.IntroTestTags

class IntroRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun clickWelcomeButton() = click(IntroTestTags.INTRO_WELCOME_SCREEN_NEXT_BUTTON)

    fun clickEnergyButton() = click(IntroTestTags.INTRO_MOTIVATION_ENERGY_SCREEN_NEXT_BUTTON)

    fun clickPrivacyButton() =
        click(IntroTestTags.INTRO_MOTIVATION_PRIVACY_SCREEN_NEXT_BUTTON)

    fun clickRecommendationButton() =
        click(IntroTestTags.INTRO_RECOMMENDATION_SCREEN_NEXT_BUTTON)

    fun clickBatteryStartButton() =
        click(IntroTestTags.INTRO_BATTERY_OPTIMISATION_SCREEN_START_BUTTON)

    fun isNotOnboarding() = isNotAnyTestTag(*IntroTestTags.INTRO_SCREENS)


}