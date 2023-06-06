package com.tomasrepcik.blumodify.helpers

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.rules.TestWatcher
import org.junit.runner.Description


class AndroidLogMockRule: TestWatcher() {
    override fun starting(description: Description?) {
        mockLogCat()
        super.starting(description)
    }

    private fun mockLogCat() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }
}