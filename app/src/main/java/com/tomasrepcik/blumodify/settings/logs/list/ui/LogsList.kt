package com.tomasrepcik.blumodify.settings.logs.list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.tomasrepcik.blumodify.app.ui.previews.AllScreenPreview
import com.tomasrepcik.blumodify.app.ui.theme.BluModifyTheme
import com.tomasrepcik.blumodify.settings.SettingsTestTags
import com.tomasrepcik.blumodify.settings.logs.list.LogUiListItem

@Composable
fun LogsList(logs: List<LogUiListItem>, onItemClick: (id: Int) -> Unit) {
    Box {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .testTag(SettingsTestTags.LOGS_LIST)) {
            items(logs) {
                LogReportCompItem(log = it, onClick = onItemClick)
            }
        }
    }
}

@AllScreenPreview
@Composable
fun LogsListPreview() {
    BluModifyTheme {
        Surface {
            LogsList(
                logs = arrayListOf(
                    LogUiListItem(0, "1.1.1990 20:00", true, "1"),
                    LogUiListItem(0, "1.1.1990 20:00", true, "1"),
                    LogUiListItem(0, "1.1.1990 20:00", true, "1"),
                    LogUiListItem(0, "1.1.1990 20:00", true, "1"),
                )
            ) {}
        }
    }
}