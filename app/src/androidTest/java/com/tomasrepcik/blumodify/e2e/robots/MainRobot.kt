package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.ComposeTestRule

class MainRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkIdling() {
        checkButtonTurnOn()
        checkAnimationOff()
    }

    fun checkWorking() {
        checkCheckButtonOff()
        checkAnimationOn()
    }

    private fun checkAnimationOff() = composeRule.onNode(
        hasStateDescription("Turned off")
    ).assertExists()

    private fun checkAnimationOn() = composeRule.onNode(
        hasStateDescription("Turned on")
    ).assertExists()

    fun checkMainScreen() = composeRule.onNode(
        hasStateDescription("Turned off").or(
            hasStateDescription("Turned on").or(
                hasStateDescription("Waiting to resolve issue")
            )
        )
    ).assertExists()

    private fun checkButtonTurnOn() = assertTextButton("Turn on")

    private fun checkCheckButtonOff() = assertTextButton("Turn off")

    fun clickTurnOnButton() = clickTextButton("Turn on")
    fun clickTurnOffButton() = clickTextButton("Turn off")


}