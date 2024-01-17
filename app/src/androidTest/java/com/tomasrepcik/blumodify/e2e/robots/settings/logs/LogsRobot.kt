package com.tomasrepcik.blumodify.e2e.robots.settings.logs

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.tomasrepcik.blumodify.app.ui.AppTestTags
import com.tomasrepcik.blumodify.e2e.robots.Robot
import com.tomasrepcik.blumodify.settings.SettingsTestTags

class LogsRobot(composeRule: ComposeTestRule) : Robot(composeRule) {

    fun checkEmptyLogs(text: String) {
        wait(AppTestTags.APP_ERROR_SCREEN)
        assertContent(AppTestTags.APP_ERROR_SCREEN)
        assertContent(AppTestTags.APP_ERROR_SCREEN_BUTTON_PRIMARY)
        assertDoesNotExist(AppTestTags.APP_ERROR_SCREEN_BUTTON_SECONDARY)
        assertText(text)
    }

    fun checkLogWithId(id: Int){
        wait(SettingsTestTags.LOGS_LIST)
        assertContent(id.toString())
    }

    fun goBackError() = click(AppTestTags.APP_ERROR_SCREEN_BUTTON_PRIMARY)

    fun openLogWithId(i: Int) = click(i.toString())


}