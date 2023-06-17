@file:OptIn(ExperimentalCoroutinesApi::class)

package com.tomasrepcik.blumodify.instrumented.bluetooth.workmanager


import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.bluetooth.workmanager.BtWorkManager
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class BtWorkManagerTests {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        hiltRule.inject()
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .setWorkerFactory(workerFactory)
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testingInitOfBtWorker() = runTest {
        // ARRANGE
        val workManager = WorkManager.getInstance(context)
        val sut = BtWorkManager(workManager)


        // ACTION
        sut.initWorkers()

        // CHECK
        when (val result = sut.workersWork()){
            is AppResult.Error -> Assert.fail()
            is AppResult.Success -> Assert.assertTrue(result.data)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testingNoWorkerInitialized() = runTest {
        // ARRANGE
        val workManager = WorkManager.getInstance(context)
        val sut = BtWorkManager(workManager)

        // CHECK
        when (val result = sut.workersWork()){
            is AppResult.Error -> Assert.fail()
            is AppResult.Success -> Assert.assertFalse(result.data)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testingCancellationOfWork() = runTest {
        // ARRANGE
        val workManager = WorkManager.getInstance(context)
        val sut = BtWorkManager(workManager)

        // ACTION
        sut.initWorkers()

        // CHECK
        when (val result = sut.workersWork()){
            is AppResult.Error -> Assert.fail()
            is AppResult.Success -> Assert.assertTrue(result.data)
        }

        // ACTION
        sut.disposeWorkers()

        // CHECK
        when (val result = sut.workersWork()){
            is AppResult.Error -> Assert.fail()
            is AppResult.Success -> Assert.assertFalse(result.data)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testingFirstRun() = runTest {
        // ARRANGE
        val workManager = WorkManager.getInstance(context)
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)
        val sut = BtWorkManager(workManager)

        // ACTION
        val uuid = sut.initWorkers()
        testDriver?.setInitialDelayMet(uuid)

        // CHECK
        val workInfo = withContext(Dispatchers.IO) {
            workManager.getWorkInfoById(uuid).get()
        }
        assertThat(workInfo.state, `is`(WorkInfo.State.RUNNING))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testingEnqueuedRun() = runTest {
        // ARRANGE
        val workManager = WorkManager.getInstance(context)
        val sut = BtWorkManager(workManager)

        // ACTION
        val uuid = sut.initWorkers()

        // CHECK
        val workInfo = withContext(Dispatchers.IO) {
            workManager.getWorkInfoById(uuid).get()
        }
        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }
}