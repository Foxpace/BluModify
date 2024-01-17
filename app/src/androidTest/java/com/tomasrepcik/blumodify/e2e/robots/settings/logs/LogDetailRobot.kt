package com.tomasrepcik.blumodify.e2e.robots.settings.logs

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.SettingsTestTags
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

class LogDetailRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkLogDetailContent(devices: Array<BtItem> = emptyArray(), stackTrace: Boolean = false) {
        wait(SettingsTestTags.LOG_DETAIL)
        assertContent(SettingsTestTags.LOG_DETAIL_TIME)
        assertContent(SettingsTestTags.LOG_DETAIL_STATUS)
        assertContent(SettingsTestTags.LOG_DETAIL_DEVICES)

        if (devices.isNotEmpty()) {
            devices.forEach {
                assertText(it.deviceName)
                assertText(it.macAddress)
            }
        } else {
            assertText("0")
        }

        if (stackTrace){
            assertContent(SettingsTestTags.LOG_DETAIL_STACKTRACE)
        } else {
            assertDoesNotExist(SettingsTestTags.LOG_DETAIL_STACKTRACE)
        }
    }

}