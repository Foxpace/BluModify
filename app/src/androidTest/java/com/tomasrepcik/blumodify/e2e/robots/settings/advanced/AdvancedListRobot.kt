package com.tomasrepcik.blumodify.e2e.robots.settings.advanced

import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem

class AdvancedListRobot(composeRule: ComposeTestRule) : Robot(composeRule) {
    fun checkAdvancedEmptyListScreen() {
        waitFor(hasContentDescription("Sad face"))
        assertContentDescription("Sad face")
        assertTextBesideImage(
            "The app does not track any device, please add at least one!", "Sad face"
        )
        assertTextButton("Add device")
    }

    fun checkAdvancedListScreenWithDeviceMac(btItem: BtItem) {
        waitFor(hasText(btItem.macAddress))
        composeRule.onNode(
            hasText(btItem.deviceName).and(
                hasAnySibling(hasText(btItem.macAddress))
            ).and(
                hasAnySibling(hasContentDescription("Remove device").and(hasClickAction()))
            )
        )
    }

    fun openAdder() = clickContentDescriptionWithButton("Add device")

    fun removeDevice(btItem: BtItem) = composeRule.onNode(
        hasText(btItem.deviceName).and(
            hasText(btItem.macAddress)
        ).and(hasContentDescription("Remove device"))
    ).performClick()


}