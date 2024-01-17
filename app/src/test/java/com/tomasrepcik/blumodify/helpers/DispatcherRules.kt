package com.tomasrepcik.blumodify.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description


/**
 * Example usage:
 *
 *      @get:Rule
 *      val dispatcherRule = StandardDispatcherRule()
 *
 */
@OptIn(ExperimentalCoroutinesApi::class)
class StandardDispatcherRule: TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(StandardTestDispatcher())
        super.starting(description)
    }



    override fun finished(description: Description?) {
        Dispatchers.resetMain()
        super.finished(description)
    }

}

@Suppress("unused")
@OptIn(ExperimentalCoroutinesApi::class)
class UnconfinedDispatcherRule: TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        super.starting(description)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
        super.finished(description)
    }

}