package com.tomasrepcik.blumodify.e2e.robots.settings

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.SettingsTestTags

class MainSettingsRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkScreenContentWithoutAdvanced() {
        assertContent(SettingsTestTags.SETTINGS_MAIN_ADVANCED)
        assertContent(SettingsTestTags.SETTINGS_MAIN_ADVANCED_CHECKMARK)
        assertContent(SettingsTestTags.SETTINGS_MAIN_WHITELIST)
        assertContent(SettingsTestTags.SETTINGS_MAIN_LOGS)

        assertDoesNotExist(SettingsTestTags.SETTINGS_MAIN_ADVANCED_ADD)
        assertDoesNotExist(SettingsTestTags.SETTINGS_MAIN_ADVANCED_ABOUT)
    }

    fun checkScreenContentWithAdvanced() {
        assertContent(SettingsTestTags.SETTINGS_MAIN_ADVANCED)
        assertContent(SettingsTestTags.SETTINGS_MAIN_ADVANCED_CHECKMARK)
        assertContent(SettingsTestTags.SETTINGS_MAIN_WHITELIST)
        assertContent(SettingsTestTags.SETTINGS_MAIN_LOGS)
        assertContent(SettingsTestTags.SETTINGS_MAIN_ADVANCED_ADD)
        assertContent(SettingsTestTags.SETTINGS_MAIN_ADVANCED_ABOUT)
    }

    fun enableAdvanced() = click(SettingsTestTags.SETTINGS_MAIN_ADVANCED_CHECKMARK)

    fun openLogs() = click(SettingsTestTags.SETTINGS_MAIN_LOGS)
    fun openAdvancedList() = click(SettingsTestTags.SETTINGS_MAIN_ADVANCED_ADD)


}