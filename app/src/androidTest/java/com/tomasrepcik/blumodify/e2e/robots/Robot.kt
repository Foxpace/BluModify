package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick

abstract class Robot(private val composeRule: ComposeTestRule) {

    fun clickButton(testTag: String) = composeRule.onNodeWithTag(testTag).performClick()

    fun assertContent(testTag: String) = composeRule.onNodeWithTag(testTag).assertExists()

    fun isNotAnyTestTag(vararg tags: String) =
        tags.forEach { composeRule.onNodeWithTag(it).assertDoesNotExist() }


}