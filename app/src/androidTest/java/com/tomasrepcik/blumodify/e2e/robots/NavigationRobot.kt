package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule

class NavigationRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    private fun openDrawer() {
        assertContentDescriptionWithButton("Menu navigation button")
        clickContentDescriptionWithButton("Menu navigation button")
    }

    private fun checkDrawer() {
        assertContentDescription("BluModify app icon")
        assertTextBesideImage("BluModify", "BluModify app icon")
        assertTextButtonWithIcon("Home", "Home navigation button")
        assertTextButtonWithIcon("Settings", "Settings navigation button")
        assertTextButtonWithIcon("About", "About navigation button")
    }

    fun openAndCheckDrawer() {
        openDrawer()
        checkDrawer()
    }

    fun openMainScreenViaDrawer() {
        openAndCheckDrawer()
        clickButtonByText("Home")
    }

    fun openSettingsScreenViaDrawer() {
        openAndCheckDrawer()
        clickButtonByText("Settings")
    }

    fun openAboutScreenViaDrawer() {
        openAndCheckDrawer()
        clickButtonByText("About")
    }

}