package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule

class IntroRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    private fun clickNextButton() = clickTextButton("Next")
    private fun clickStartButton() = clickTextButton("Start")

    fun passIntro() {
        clickNextButton()
        clickNextButton()
        clickNextButton()
        clickNextButton()
        clickStartButton()
    }

}