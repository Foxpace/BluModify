package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.ComposeTestRule

class MainRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkIdling() {
        checkAnimationOff()
        assertText("Optimise Bluetooth usage")
        assertTextButton("Turn on")
    }

    fun checkWorking() {
        checkAnimationOn()
        assertText("Checking Bluetooth perpetually")
        assertTextButton("Turn off")
    }

    fun checkMainScreen() = composeRule.onNode(
        hasStateDescription("Turned off").or(
            hasStateDescription("Turned on")
        ).or(
            hasStateDescription("Waiting to resolve issue")
        )
    ).assertExists()

    fun clickTurnOnButton() = clickTextButton("Turn on")
    fun clickTurnOffButton() = clickTextButton("Turn off")


    private fun checkAnimationOff() = composeRule.onNode(
        hasStateDescription("Turned off")
    ).assertExists()

    private fun checkAnimationOn() = composeRule.onNode(
        hasStateDescription("Turned on")
    ).assertExists()

}