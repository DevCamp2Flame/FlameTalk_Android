package com.sgs.devcamp2.flametalk_android

import android.app.Activity
import android.app.Application
import android.content.Context
import com.sgs.devcamp2.flametalk_android.util.FlameTalkActivityLifecycleCallbacks
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FlameTalkApp : Application() {
    val instance = this
    val activityLifecycleCallbacks = FlameTalkActivityLifecycleCallbacks()

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
}
