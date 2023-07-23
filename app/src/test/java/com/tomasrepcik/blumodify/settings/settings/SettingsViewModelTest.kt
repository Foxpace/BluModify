package com.tomasrepcik.blumodify.settings.settings

import com.tomasrepcik.blumodify.app.model.AppResult
import com.tomasrepcik.blumodify.app.model.ErrorCause
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheState
import com.tomasrepcik.blumodify.app.storage.cache.AppCacheTemplate
import com.tomasrepcik.blumodify.app.storage.cache.AppSettings
import com.tomasrepcik.blumodify.helpers.AndroidLogMockRule
import com.tomasrepcik.blumodify.helpers.StandardDispatcherRule
import com.tomasrepcik.blumodify.settings.settings.states.SettingsEvent
import com.tomasrepcik.blumodify.settings.settings.states.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(JUnit4::class)
class SettingsViewModelTest {

    @get:Rule(order = Integer.MIN_VALUE)
    val dispatcherRule = StandardDispatcherRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val androidLogs = AndroidLogMockRule()

    @get:Rule(order = Integer.MIN_VALUE)
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var appCacheTemplate: AppCacheTemplate<AppCacheState>

    private lateinit var sut: SettingsViewModel

    @Before
    fun setUp() {
        appCacheTemplate.stub {
            on { state } doAnswer { MutableStateFlow<AppCacheState>(AppCacheState.Loading).asStateFlow() }
        }
        sut = SettingsViewModel(appCacheTemplate)
    }

    @Test
    fun `Initial state`() {
        assertEquals(SettingsState.SettingsLoading, sut.settingsState.value)
    }

    @Test
    fun `On settings loaded`() {
        // ARRANGE
        val settings = AppSettings(isOnboarded = true, isAdvancedSettings = true)

        // ACTION
        sut.onSettingsChange(AppCacheState.Loaded(settings))

        // CHECK
        assertEquals(SettingsState.SettingsLoaded(settings), sut.settingsState.value)
    }

    @Test
    fun `On settings error`() {
        // ARRANGE
        val error = AppResult.Error<ErrorCause>(
            message = "error", origin = "appcache", errorCause = ErrorCause.MISSING_SETTINGS, null
        )

        // ACTION
        sut.onSettingsChange(AppCacheState.Error(error))

        // CHECK
        assertEquals(SettingsState.SettingsError(error), sut.settingsState.value)
    }

    @Test
    fun `On toggle advanced settings`() = runTest {
        // ARRANGE
        val settings = AppSettings(isOnboarded = true, isAdvancedSettings = true)
        appCacheTemplate.stub {
            onBlocking { storeAdvancedSettings(false) } doAnswer {
                sut.onSettingsChange(AppCacheState.Loaded(settings.copy(isAdvancedSettings = false)))
            }
        }

        // ACTION
        sut.onSettingsChange(AppCacheState.Loaded(settings))
        sut.onEvent(SettingsEvent.ToggleAdvancedSettings).join()


        // CHECK
        assertEquals(
            SettingsState.SettingsLoaded(settings.copy(isAdvancedSettings = false)),
            sut.settingsState.value
        )
        verify(appCacheTemplate, times(1)).storeAdvancedSettings(false)
    }

}