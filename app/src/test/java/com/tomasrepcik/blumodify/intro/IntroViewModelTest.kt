package com.tomasrepcik.blumodify.intro

import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.stub

class IntroViewModelTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @Mock
    private var appCache: AppCacheTemplate<AppCacheState> = mock()

    private lateinit var sut: IntroViewModel

    @Before
    fun setUp(){
        appCache.stub {
            onBlocking { this.loadInCacheAsync() } doAnswer {}
            on { this.state } doAnswer { MutableStateFlow<AppCacheState>(AppCacheState.Loading).asStateFlow() }
        }
        sut = IntroViewModel(appCache)
    }

    @After
    fun tearDown(){
        reset(appCache)
    }

    @Test
    fun `Initial state`() = runTest {
        assertTrue(sut.isLoading.value)
        assertFalse(sut.isOnboarded.value)
    }

    @Test
    fun `Loading is successful`() = runTest {

        // ACTION
        sut.onAppCacheState(AppCacheState.Loaded(AppSettings(true, isAdvancedSettings = true)))

        // CHECK
        assertFalse(sut.isLoading.value)
        assertTrue(sut.isOnboarded.value)
    }

    @Test
    fun `Saving the onboarding`() = runTest {
        // ARRANGE
        appCache.stub {
            onBlocking { this.storeOnboarding(true) } doAnswer {
                sut.onAppCacheState(AppCacheState.Loaded(AppSettings(true, isAdvancedSettings = true)))
            }
        }

        // ACTION
        val job = sut.saveUserOnboarding()
        job.join()

        // CHECK
        assertFalse(sut.isLoading.value)
        assertTrue(sut.isOnboarded.value)
    }

}