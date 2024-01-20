package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule

class ErrorRobot(composeRule: ComposeTestRule) :
    Robot(composeRule) {

    fun checkErrorScreen(issueDescription: String, primaryButtonText: String) {
        assertImage("Sad face")
        assertTextBesideImage(issueDescription, "Sad face")
        assertTextButton(primaryButtonText)
    }

    fun resolveIssueByClickingButton(buttonText: String, beforeClick: (() -> Unit)? = null) {
        beforeClick?.invoke()
        clickButtonByText(buttonText)
    }

}