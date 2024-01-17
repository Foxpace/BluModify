package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.about.AboutTestTags

class AboutRobot(composeRule: ComposeTestRule) :
    Robot(composeRule) {

    fun checkAboutScreenContent() {
        AboutTestTags.ABOUT_SCREEN_CONTENT.forEach {
            assertContent(it)
        }
    }


}