package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.app.ui.AppTestTags

class ErrorRobot(composeRule: ComposeTestRule) :
    Robot(composeRule) {

    fun checkErrorScreen() {
        assertContent(AppTestTags.APP_ERROR_SCREEN)
        assertContent(AppTestTags.APP_ERROR_SCREEN_BUTTON_PRIMARY)
    }

    fun noDetailsButton() = assertDoesNotExist(AppTestTags.APP_ERROR_SCREEN_BUTTON_DETAIL)


    fun resolveIssue(beforeClick: (() -> Unit)? = null) {
        beforeClick?.invoke()
        click(AppTestTags.APP_ERROR_SCREEN_BUTTON_PRIMARY)
    }

}