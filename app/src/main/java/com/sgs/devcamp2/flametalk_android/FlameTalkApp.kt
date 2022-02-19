package com.sgs.devcamp2.flametalk_android

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.sgs.devcamp2.flametalk_android.util.FlameTalkActivityLifecycleCallbacks
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FlameTalkApp : Application(), Configuration.Provider {
    val instance = this
    val activityLifecycleCallbacks = FlameTalkActivityLifecycleCallbacks()

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(
            Timber.DebugTree()
        )

    }

    companion object {
        var appContext: Context? = null
        lateinit var instance: FlameTalkApp

        fun applicationContext(): Context {
            return instance.applicationContext
        }

        fun currentActivity(): Activity? {
            return instance.activityLifecycleCallbacks.currentActivity
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().setWorkerFactory(workerFactory)
            .build()
}
