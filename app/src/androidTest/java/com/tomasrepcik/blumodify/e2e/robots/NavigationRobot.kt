package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule

class NavigationRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    private fun openDrawer() {
        assertIconButton("Menu navigation button")
        clickIconButton("Menu navigation button")
    }

    private fun checkDrawer() {
        assertImage("BluModify app icon")
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
        clickTextButton("Home")
    }

    fun openSettingsScreenViaDrawer() {
        openAndCheckDrawer()
        clickTextButton("Settings")
    }

    fun openAboutScreenViaDrawer() {
        openAndCheckDrawer()
        clickTextButton("About")
    }

}