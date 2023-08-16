package com.tomasrepcik.blumodify.e2e.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule

/**
 * https://stackoverflow.com/questions/68267861/add-intent-extras-in-compose-ui-test
 * Uses a [ComposeTestRule] created via [createEmptyComposeRule] that allows setup before the activity
 * is launched via [onBefore].
 */
inline fun <reified A : Activity> launchApp(
    onBefore: () -> Unit = {},
    intentFactory: (Context) -> Intent = {
        Intent(
            ApplicationProvider.getApplicationContext(), A::class.java
        )
    },
): ActivityScenario<A> {
    onBefore()
    val context = ApplicationProvider.getApplicationContext<Context>()
    return ActivityScenario.launch(intentFactory(context))
}

/**
 * Factory method to provide android specific implementation of createComposeRule, for a given
 * activity class type A that needs to be launched via an intent.
 *
 * @param intentFactory A lambda that provides a Context that can used to create an intent. A intent needs to be returned.
 */
inline fun <A : ComponentActivity> createAndroidIntentComposeRule(intentFactory: (context: Context) -> Intent): AndroidComposeTestRule<ActivityScenarioRule<A>, A> {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val intent = intentFactory(context)

    return AndroidComposeTestRule(activityRule = ActivityScenarioRule(intent),
        activityProvider = { scenarioRule -> scenarioRule.getActivity() })
}

/**
 * Gets the activity from a scenarioRule.
 *
 * https://androidx.tech/artifacts/compose.ui/ui-test-junit4/1.0.0-alpha11-source/androidx/compose/ui/test/junit4/AndroidComposeTestRule.kt.html
 */
fun <A : ComponentActivity> ActivityScenarioRule<A>.getActivity(): A {
    var activity: A? = null

    scenario.onActivity { activity = it }

    return activity
        ?: throw IllegalStateException("Activity was not set in the ActivityScenarioRule!")
}