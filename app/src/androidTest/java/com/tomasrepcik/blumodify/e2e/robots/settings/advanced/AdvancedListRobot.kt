package com.tomasrepcik.blumodify.e2e.robots.settings.advanced

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.app.ui.AppTestTags
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.SettingsTestTags

class AdvancedListRobot(composeRule: ComposeTestRule) : Robot(composeRule) {
    fun checkAdvancedEmptyListScreen() {
        assertContent(SettingsTestTags.ADVANCED_LIST_SCREEN)
        wait(AppTestTags.APP_ERROR_SCREEN)
        assertContent(AppTestTags.APP_ERROR_SCREEN)
    }

    fun checkAdvancedListScreenWithDeviceMac(macAddress: String) {
        assertContent(SettingsTestTags.ADVANCED_LIST_SCREEN)
        wait(SettingsTestTags.ADVANCED_LIST_TO_DELETE)
        assertContent(SettingsTestTags.ADVANCED_LIST_TO_DELETE)
        assertText(macAddress)
    }

    fun openAdder() = click(SettingsTestTags.ADVANCED_ADD_DEVICE_BUTTON)

    fun removeDevice(macAddress: String) = clickByText(macAddress)


}