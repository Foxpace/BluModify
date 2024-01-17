package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.home.HomeTestTags

class MainRobot(composeRule: ComposeTestRule) :
    Robot(composeRule) {

    fun checkIdling() {
        checkButtonTurnOn()
        checkMainScreen()
    }

    fun checkWorking() {
        checkCheckButtonOff()
        checkMainScreen()
    }

    fun checkMainScreen() = assertContent(HomeTestTags.HOME_SCREEN_MAIN_ANIMATION)

    private fun checkButtonTurnOn() = assertContent(HomeTestTags.HOME_BUTTON_ON)

    private fun checkCheckButtonOff() = assertContent(HomeTestTags.HOME_BUTTON_OFF)

    fun clickTurnOnButton() = click(HomeTestTags.HOME_BUTTON_ON)
    fun clickTurnOffButton() = click(HomeTestTags.HOME_BUTTON_OFF)


}