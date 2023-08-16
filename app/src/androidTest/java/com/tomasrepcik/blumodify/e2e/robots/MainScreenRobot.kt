package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.home.HomeTestTags

class MainScreenRobot(composeRule: ComposeTestRule) :
    Robot(composeRule) {

    fun checkMainScreen() = assertContent(HomeTestTags.HOME_SCREEN_MAIN_ANIMATION)


}