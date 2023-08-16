package com.tomasrepcik.blumodify.e2e.helpers

import android.content.Context
import android.util.Log
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.work.Configuration
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.e2e.helpers.config.TestConfig
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@HiltAndroidTest
abstract class UiTest(testConfig: TestConfig) {

    @Suppress("LeakingThis")
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val permissionsRule: GrantPermissionRule = GrantPermissionRule.grant(*testConfig.permissions)

    @get:Rule(order = 2)
    val composeTestRule = createEmptyComposeRule()

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var cache: AppCacheTemplate<AppCacheState>

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    fun clearCache() = runBlocking {
        cache.setDefault()
    }

    private fun setUpWorkManager() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .setWorkerFactory(workerFactory)
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Before
    open fun setUp() {
        hiltRule.inject()
        setUpWorkManager()
    }
}