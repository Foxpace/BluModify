package com.tomasrepcik.blumodify.e2e.robots.settings.advanced

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.SettingsTestTags

class BtPickerRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkMissingBtScreen() {
        assertContent(SettingsTestTags.ADVANCED_BT_PICKER_SCREEN)
        wait(SettingsTestTags.ADVANCED_BT_OFF_SCREEN)
        assertContent(SettingsTestTags.ADVANCED_BT_OFF_SCREEN)
    }

    fun checkMissingPermissionScreen() {
        assertContent(SettingsTestTags.ADVANCED_BT_PICKER_SCREEN)
        wait(SettingsTestTags.ADVANCED_MISSING_BT_PERMISSION)
        assertContent(SettingsTestTags.ADVANCED_MISSING_BT_PERMISSION)
    }

    fun clickToTurnOnBt() = click(SettingsTestTags.ADVANCED_TURN_ON_BT_BUTTON)

    fun checkNoPairedDevicesScreen() {
        wait(SettingsTestTags.ADVANCED_NO_PAIRED_DEVICES_SCREEN)
        assertContent(SettingsTestTags.ADVANCED_NO_PAIRED_DEVICES_SCREEN)
    }

    fun checkAllDevicesAddedScreen() {
        wait(SettingsTestTags.ADVANCED_NO_PAIRED_DEVICES_SCREEN)
        assertContent(SettingsTestTags.ADVANCED_NO_PAIRED_DEVICES_SCREEN)
    }

    fun checkPairedDevicesScreenWithMacAddress(macAddress: String) {
        wait(SettingsTestTags.ADVANCED_BT_PICKER_LIST)
        assertContent(SettingsTestTags.ADVANCED_BT_PICKER_LIST)
        assertText(macAddress)
    }

    fun addDevice(macAddress: String) = clickByText(macAddress)


}