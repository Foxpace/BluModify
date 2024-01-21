package com.tomasrepcik.blumodify.e2e.robots

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick

abstract class Robot(open val composeRule: ComposeTestRule) {

    fun clickButtonByText(text: String) = composeRule.onNode(hasTextExactly(text)).performClick()

    @OptIn(ExperimentalTestApi::class)
    fun waitFor(matcher: SemanticsMatcher) = composeRule.waitUntilExactlyOneExists(matcher)

    fun assertContentDescription(description: String) =
        composeRule.onNode(hasContentDescription(description)).assertExists()

    fun assertContentDescriptionWithButton(description: String) =
        composeRule.onNode(hasContentDescription(description).and(hasClickAction())).assertExists()

    fun clickContentDescriptionWithButton(description: String) = composeRule.onNode(
        hasContentDescription(description).and(
            hasClickAction()
        )
    ).performClick()

    fun assertText(text: String, ignoreCase: Boolean = false, substring: Boolean = false) =
        composeRule.onNode(hasText(text, ignoreCase = ignoreCase, substring = substring))
            .assertExists()

    fun assertDoesNotExistText(text: String) =
        composeRule.onNode(hasText(text)).assertDoesNotExist()

    fun assertTextBesideImage(text: String, description: String) {
        composeRule.onNode(
            hasText(text).and(
                hasAnySibling(hasContentDescription(description))
            )
        )
    }

    fun assertTextButton(text: String) = composeRule.onNode(hasText(text).and(hasClickAction()))

    fun assertTextButtonWithIcon(text: String, description: String) = composeRule.onNode(
        hasText(text).and(hasClickAction()).and(
            hasAnySibling(hasClickAction().and(hasContentDescription(description)))
        )
    )

    fun goBack() = clickContentDescriptionWithButton("Back button")

}