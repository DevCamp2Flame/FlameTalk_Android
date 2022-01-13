package com.sgs.devcamp2.flametalk_android

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class FlameTalkApp:Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(
            Timber.DebugTree()
        )
    }
    companion object{
        var appContext: Context? = null
    }
}