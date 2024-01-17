package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.tomasrepcik.blumodify.app.ui.AppTestTags

abstract class Robot(private val composeRule: ComposeTestRule) {

    fun click(testTag: String) = composeRule.onNodeWithTag(testTag).performClick()

    fun clickByText(text: String) = composeRule.onNodeWithText(text).performClick()

    @OptIn(ExperimentalTestApi::class)
    fun wait(testTag: String) = composeRule.waitUntilExactlyOneExists(hasTestTag(testTag))

    fun assertContent(testTag: String) = composeRule.onNodeWithTag(testTag).assertExists()

    fun assertText(text: String) = composeRule.onNodeWithText(text).assertExists()

    fun assertDoesNotExist(testTag: String) =
        composeRule.onNodeWithTag(testTag).assertDoesNotExist()

    fun isNotAnyTestTag(vararg tags: String) =
        tags.forEach { composeRule.onNodeWithTag(it).assertDoesNotExist() }

    fun goBack() = click(AppTestTags.APP_BACK_BUTTON)


}