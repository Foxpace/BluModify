package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule

class IntroRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    private fun clickNextButton() = clickButtonByText("Next")
    private fun clickStartButton() = clickButtonByText("Start")

    fun passIntro() {
        clickNextButton()
        clickNextButton()
        clickNextButton()
        clickNextButton()
        clickStartButton()
    }

}