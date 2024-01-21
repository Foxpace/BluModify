package com.tomasrepcik.blumodify.e2e.settings.logs

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.app.storage.DataStoreDI
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.app.storage.room.dao.LogsDao
import com.tomasrepcik.blumodify.app.storage.room.entities.LogReport
import com.tomasrepcik.blumodify.e2e.helpers.UiTest
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import com.tomasrepcik.blumodify.e2e.helpers.launchApp
import com.tomasrepcik.blumodify.e2e.robots.NavigationRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.MainSettingsRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.logs.LogDetailRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.logs.LogsRobot
import com.tomasrepcik.blumodify.settings.advanced.shared.model.BtItem
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DataStoreDI::class)
class NotEmptyLogsTest : UiTest(TestConfig.AllPermissions) {

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true))

    @Inject
    lateinit var logsDao: LogsDao

    private val btDevice1 = BtItem(deviceName = "device 1", macAddress = "AA:AA")
    private val btDevice2 = BtItem(deviceName = "device 2", macAddress = "00:00")
    private val logReport = LogReport(
        startTime = 0L,
        connectedDevices = listOf(btDevice1, btDevice2),
        isSuccess = false,
        stackTrace = null
    )

    override fun setUp() {
        super.setUp()
        runBlocking(Dispatchers.IO) {
            logsDao.insertReport(logReport)
        }
    }

    @Test
    fun checkLogsTest() {


        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithoutAdvanced()
            openLogs()
        }

        with(LogsRobot(composeTestRule)) {
            checkLogItem(logReport)
            goBack()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithoutAdvanced()
        }
    }

    @Test
    fun openAndCheckLogDetails() {

        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithoutAdvanced()
            openLogs()
        }

        with(LogsRobot(composeTestRule)) {
            checkLogItem(logReport)
            openLogWithId(logReport)
        }

        with(LogDetailRobot(composeTestRule)) {
            checkLogDetailContent(logReport)
            goBack()
        }

        with(LogsRobot(composeTestRule)) {
            goBack()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithoutAdvanced()
        }
    }
}