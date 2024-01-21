package com.tomasrepcik.blumodify.e2e.settings.advanced.btlist

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.app.storage.DataStoreDI
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.app.storage.room.dao.BtDeviceDao
import com.tomasrepcik.blumodify.app.storage.room.entities.BtDevice
import com.tomasrepcik.blumodify.e2e.helpers.UiTest
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import com.tomasrepcik.blumodify.e2e.helpers.launchApp
import com.tomasrepcik.blumodify.e2e.robots.NavigationRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.MainSettingsRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.advanced.AdvancedListRobot
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
class AdvancedListTest : UiTest(TestConfig.AllPermissions) {

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true))

    @Inject
    lateinit var deviceDao: BtDeviceDao

    private val btDeviceDao = BtDevice("00:00", "device", false, 0L)
    private val btItem = BtItem(btDeviceDao.name, btDeviceDao.macAddress)

    @Test
    fun checkAdvancedWithDeviceTest() {

        runBlocking(Dispatchers.IO) {
            deviceDao.insertBtDevice(btDeviceDao)
        }

        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
        }

        with(MainSettingsRobot(composeTestRule)) {
            enableAdvanced()
            openAdvancedList()
        }

        with(AdvancedListRobot(composeTestRule)) {
            checkAdvancedListScreenWithDeviceMac(btItem)
            goBack()
        }

        with(MainSettingsRobot(composeTestRule)) {
            checkScreenContentWithAdvanced()
        }
    }
}