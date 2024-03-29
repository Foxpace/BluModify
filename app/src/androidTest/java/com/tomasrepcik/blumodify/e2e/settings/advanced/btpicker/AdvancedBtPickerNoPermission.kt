package com.tomasrepcik.blumodify.e2e.settings.advanced.btpicker

import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tomasrepcik.blumodify.MainActivity
import com.tomasrepcik.blumodify.app.storage.DataStoreDI
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.bluetooth.controller.BtControllerTemplate
import com.tomasrepcik.blumodify.bluetooth.di.BluetoothControllerDI
import com.tomasrepcik.blumodify.e2e.helpers.UiTest
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import com.tomasrepcik.blumodify.e2e.helpers.launchApp
import com.tomasrepcik.blumodify.e2e.robots.NavigationRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.MainSettingsRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.advanced.AdvancedListRobot
import com.tomasrepcik.blumodify.e2e.robots.settings.advanced.BtPickerRobot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DataStoreDI::class, BluetoothControllerDI::class)
class AdvancedBtPickerNoPermission : UiTest(TestConfig.AllPermissions) {

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true))

    @BindValue
    @JvmField
    val btController: BtControllerTemplate = mock<BtControllerTemplate>().stub {
        on { isPermission() } doReturn(false)
    }

    @Test
    fun checkEmptyAdvanced_OpenAdderList_MissingPermission() {

        launchApp<MainActivity>()

        with(NavigationRobot(composeTestRule)) {
            openSettingsScreenViaDrawer()
        }

        with(MainSettingsRobot(composeTestRule)) {
            enableAdvancedTracking()
            openAdvancedList()
        }

        with(AdvancedListRobot(composeTestRule)) {
            checkAdvancedEmptyListScreen()
            openAdder()
        }

        with(BtPickerRobot(composeTestRule)) {
            checkMissingPermissionScreen()
        }
    }
}