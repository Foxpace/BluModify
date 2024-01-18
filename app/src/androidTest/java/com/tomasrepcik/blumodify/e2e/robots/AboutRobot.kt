package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule

class AboutRobot(composeRule: ComposeTestRule) :
    Robot(composeRule) {


    fun checkAboutScreen() {
        assertTextBesideImage("BluModify", "BluModify")
        assertText("The BluModify serves to check on the Bluetooth activity", substring = true)
        assertText("Version 1.0.0 / 1")
        assertTextButtonWithIcon("Check app's repository", "Github icon")
        assertTextButtonWithIcon("Open Source Licenses", "License icon")
    }

}