package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.tomasrepcik.blumodify.app.ui.AppTestTags

abstract class Robot(open val composeRule: ComposeTestRule) {

    fun click(testTag: String) = composeRule.onNodeWithTag(testTag).performClick()

    fun clickByText(text: String) = composeRule.onNodeWithText(text).performClick()

    @OptIn(ExperimentalTestApi::class)
    fun wait(testTag: String) = composeRule.waitUntilExactlyOneExists(hasTestTag(testTag))

    fun assertContent(testTag: String) = composeRule.onNodeWithTag(testTag).assertExists()

    fun assertContentWithDescription(description: String) =
        composeRule.onNode(hasContentDescription(description)).assertExists()

    fun assertText(text: String, ignoreCase: Boolean = false, substring: Boolean = false) =
        composeRule.onNode(hasText(text, ignoreCase = ignoreCase, substring = substring))
            .assertExists()

    fun assertTextBesideImage(text: String, description: String) =
        composeRule.onNode(hasText(text).and(hasAnySibling(hasContentDescription(description))))

    fun assertTextButton(text: String) = composeRule.onNode(hasText(text).and(hasClickAction()))

    fun assertTextButtonWithIcon(text: String, description: String) = composeRule.onNode(
        hasText(text).and(hasClickAction()).and(
            hasAnySibling(hasClickAction().and(hasContentDescription(description)))
        )
    )

    fun assertDoesNotExist(testTag: String) =
        composeRule.onNodeWithTag(testTag).assertDoesNotExist()

    fun isNotAnyTestTag(vararg tags: String) =
        tags.forEach { composeRule.onNodeWithTag(it).assertDoesNotExist() }

    fun goBack() = click(AppTestTags.APP_BACK_BUTTON)


}