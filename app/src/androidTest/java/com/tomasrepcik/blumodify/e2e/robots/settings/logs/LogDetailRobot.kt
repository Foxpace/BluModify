package com.tomasrepcik.blumodify.e2e.robots.settings.logs

import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.logs.detail.LogReportUiItem
import java.text.DateFormat

class LogDetailRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkLogDetailContent(logReport: LogReport) {
        val logReportUi = logReportToUi(logReport)

        waitFor(hasTextExactly(logReportUi.time))
        assertText("Time")
        assertText(logReportUi.time)
        assertText("Result")
        assertText(if (logReportUi.isSuccess) "Success" else "Failure")
        assertText("Connected devices")
        logReportUi.connectedDevices.forEach {
            composeRule.onNode(hasText(it.deviceName).and(hasAnySibling(hasText(it.macAddress))))
        }

        if (logReportUi.stackTrace.isNotBlank()) {
            assertText("Error")
            assertText(logReportUi.stackTrace)
        } else {
            assertDoesNotExistText("Error")
            composeRule.onNode(hasText("Error").and(hasAnySibling(hasText(logReportUi.stackTrace))))
                .assertDoesNotExist()
        }
    }

    private fun logReportToUi(log: LogReport): LogReportUiItem = LogReportUiItem(
        log.id.toString(),
        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(log.startTime),
        log.isSuccess,
        log.connectedDevices.toTypedArray(),
        log.stackTrace ?: ""
    )

}