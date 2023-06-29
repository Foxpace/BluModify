package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.tomasrepcik.blumodify.MainActivity

abstract class Robot(private val composeRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {

    fun clickButton(testTag: String) = composeRule.onNodeWithTag(testTag).performClick()

    fun assertContent(testTag: String) = composeRule.onNodeWithTag(testTag).assertExists()


}