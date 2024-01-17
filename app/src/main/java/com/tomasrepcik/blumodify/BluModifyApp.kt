package com.tomasrepcik.blumodify

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BluModifyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() {
            val builder = Configuration.Builder().setWorkerFactory(workerFactory)
            if (BuildConfig.DEBUG) {
                // logcat - search for "WM-"
                builder.setMinimumLoggingLevel(android.util.Log.DEBUG)
            }
            return builder.build()
        }


}
