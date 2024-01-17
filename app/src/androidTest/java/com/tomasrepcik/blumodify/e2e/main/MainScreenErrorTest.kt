package com.tomasrepcik.blumodify.e2e.main

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
import com.tomasrepcik.blumodify.e2e.robots.ErrorRobot
import com.tomasrepcik.blumodify.e2e.robots.MainRobot
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturnConsecutively
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub


@LargeTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(DataStoreDI::class, BluetoothControllerDI::class)
class MainScreenErrorTest : UiTest(TestConfig.AllPermissions) {

    override val hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val appSettings: DataStore<AppSettings> =
        generateSettingsMock(appSettings = AppSettings(isOnboarded = true))

    @BindValue
    @JvmField
    val btController: BtControllerTemplate = mock<BtControllerTemplate>().stub {
        on { isPermission() } doReturnConsecutively(listOf(false, true))
    }

    @Test
    fun tryToTurnOnJobWithoutPermission() {

        launchApp<MainActivity>()

        with(MainRobot(composeTestRule)) {
            checkIdling()
            clickTurnOnButton()
        }

        with(ErrorRobot(composeTestRule)){
            checkErrorScreen()
            resolveIssue()
        }

        with(MainRobot(composeTestRule)) {
            checkWorking()
        }


    }


}