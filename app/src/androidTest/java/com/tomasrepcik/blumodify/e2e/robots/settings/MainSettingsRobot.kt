package com.tomasrepcik.blumodify.e2e.robots.settings

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.e2e.robots.Robot

class MainSettingsRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkScreenContentWithoutAdvancedTracking() {
        assertText("Devices")
        assertTextButtonWithIcon("Advanced tracking", "Bluetooth button")
        assertText("History")
        assertTextButtonWithIcon("Execution logs", "Execution logs button")
        assertText("Battery routines")
        assertTextButtonWithIcon("Add to whitelist", "Happy battery")

        assertDoesNotExistText("Explanation")
        assertDoesNotExistText("Tracked devices list")
    }

    fun checkScreenContentWithAdvancedTracking() {
        assertText("Devices")
        assertTextButtonWithIcon("Advanced tracking", "Bluetooth button")
        assertTextButtonWithIcon("Explanation", "Question mark")
        assertTextButtonWithIcon("Tracked devices list", "Check mark")
        assertTextButtonWithIcon("Advanced tracking", "Bluetooth button")
        assertText("History")
        assertTextButtonWithIcon("Execution logs", "Execution logs button")
        assertText("Battery routines")
        assertTextButtonWithIcon("Add to whitelist", "Happy battery")
    }

    fun enableAdvancedTracking() = clickTextButton("Advanced tracking")

    fun openLogs() = clickTextButton("Execution logs")
    fun openAdvancedList() = clickTextButton("Tracked devices list")


}