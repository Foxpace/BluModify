package com.tomasrepcik.blumodify.e2e.robots.settings.logs

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performClick
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.logs.list.LogUiListItem
import java.text.DateFormat

class LogsRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkEmptyLogsScreen() {
        waitFor(hasContentDescription("Sad face"))
        assertImage("Sad face")
        assertTextBesideImage(
            "No logs are in database. Use the app for a while and some logs will be recorded.",
            "Sad face"
        )
        assertTextButton("Back")
    }

    fun checkLogItem(logReport: LogReport) {
        composeRule.onNode(hasAnyChild(returnMatcher(reportToLogUi(logReport)))).assertExists()
        reportToNode(logReport).assertExists()
    }

    fun goBackFromError() = clickTextButton("Back")

    fun openLogWithId(logReport: LogReport) = reportToNode(logReport).performClick()


    private fun reportToLogUi(logReport: LogReport): LogUiListItem = LogUiListItem(
        id = logReport.id,
        time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            .format(logReport.startTime),
        isSuccess = logReport.isSuccess,
        connectedDevices = logReport.connectedDevices.size.toString()
    )

    private fun reportToNode(logReport: LogReport): SemanticsNodeInteraction =
        composeRule.onNode(returnMatcher(reportToLogUi(logReport)))

    private fun returnMatcher(logUi: LogUiListItem): SemanticsMatcher = hasText(logUi.time).and(
        hasText("Connected devices: ${logUi.connectedDevices}").and(
            hasText(
                if (logUi.isSuccess) "Success" else "Failure"
            )
        )
    )


}