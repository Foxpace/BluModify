package com.tomasrepcik.blumodify.e2e.robots.settings.advanced

import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

class BtPickerRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkMissingBtScreen() {
        waitFor(hasContentDescription("Bluetooth is off").and(hasNoClickAction()))
        assertContentDescription("Bluetooth is off")
        assertText("The Bluetooth is off", substring = true)
        assertText("To get recent list of Bluetooth devices", substring = true)
        assertTextButton("Turn on")
    }

    fun checkMissingPermissionScreen() {
        waitFor(hasContentDescription("Bluetooth no permission").and(hasNoClickAction()))
        assertText("Bluetooth permission is required to obtain access to bluetooth devices.", substring = true)
        assertTextButton("Give permission")
    }

    fun checkNoPairedDevicesScreen() {
        waitFor(hasContentDescription("No device available").and(hasNoClickAction()))
        assertContentDescription("No device available")
        assertText("There are no paired Bluetooth devices", substring = true)
        assertTextButton("Add device")
    }

    fun checkBtItemExists(btItem: BtItem) = composeRule.onNode(
        hasText(btItem.deviceName).and(hasText(btItem.macAddress)).and(
            hasContentDescription("Add device").and(hasClickAction())
        )
    ).assertExists()

    fun checkBtItemDoesNotExists(btItem: BtItem) =
        composeRule.onNode(hasText(btItem.deviceName).and(hasAnySibling(hasText(btItem.macAddress))))
            .assertDoesNotExist()


    fun addDevice(btItem: BtItem) =
        composeRule.onNode(
            hasText(btItem.deviceName).and(hasText(btItem.macAddress)).and(
                hasContentDescription("Add device").and(hasClickAction())
            )
        ).performClick()


}