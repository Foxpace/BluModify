package com.tomasrepcik.blumodify.e2e.helpers

import android.util.Log
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.datastore.core.DataStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.await
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import javax.inject.Inject

@HiltAndroidTest
abstract class UiTest(testConfig: TestConfig) {

    @get:Rule(order = 0)
    abstract val hiltRule: HiltAndroidRule

    @get:Rule(order = 1)
    val permissionsRule: GrantPermissionRule = GrantPermissionRule.grant(*testConfig.permissions)

    @get:Rule(order = 2)
    val composeTestRule = createEmptyComposeRule()

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    /**
     * @BindValue will not work here, because Hilt does not support search in subclasses
     * the BindValue does not have effect on subclasses for the tests
     * https://github.com/google/dagger/issues/3405
     */

    fun generateSettingsMock(appSettings: AppSettings = AppSettings.default): DataStore<AppSettings> =
        mock<DataStore<AppSettings>>().stub {
            onBlocking { data } doAnswer { flow { emit(appSettings) } }
        }

    private fun setUpWorkManager() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .setWorkerFactory(workerFactory)
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(
            InstrumentationRegistry.getInstrumentation().targetContext,
            config
        )
    }

    private fun cancelAllWork() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().context
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWork().await()
    }

    @Before
    open fun setUp() {
        hiltRule.inject()
        setUpWorkManager()
        cancelAllWork()
    }
}