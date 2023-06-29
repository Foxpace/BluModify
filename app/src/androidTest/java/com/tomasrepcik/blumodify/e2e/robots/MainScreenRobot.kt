package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.home.HomeTestTags

class MainScreenRobot(composeRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) :
    Robot(composeRule) {

    fun checkMainScreen() = assertContent(HomeTestTags.HOME_SCREEN_MAIN_ANIMATION)


}