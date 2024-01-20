package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.app.ui.AppTestTags
import com.tomasrepcik.blumodify.settings.SettingsTestTags

class NavigationRobot(composeRule: ComposeTestRule) :
    Robot(composeRule) {

    fun openAndCheckDrawer() {
        click(AppTestTags.APP_DRAWER_BUTTON)
        assertContent(AppTestTags.APP_DRAWER_SHEET)

    }

    fun openMainScreenViaDrawer(){
        openDrawerAndClick(AppTestTags.APP_DRAWER_SHEET_HOME)
        checkMainScreen()
    }

    fun openSettingsScreenViaDrawer(){
        openDrawerAndClick(AppTestTags.APP_DRAWER_SHEET_SETTINGS)
        checkSettingsScreen()
    }

    fun openAboutScreenViaDrawer(){
        openDrawerAndClick(AppTestTags.APP_DRAWER_SHEET_ABOUT)
    }

    private fun openDrawerAndClick(option: String) {
        click(AppTestTags.APP_DRAWER_BUTTON)
        assertContent(AppTestTags.APP_DRAWER_SHEET)
        click(option)
    }

    private fun checkMainScreen() {

    }

    private fun checkSettingsScreen() = assertContent(SettingsTestTags.SETTINGS_SCREEN)


}