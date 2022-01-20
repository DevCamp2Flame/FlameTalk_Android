package com.sgs.devcamp2.flametalk_android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Request
import javax.inject.Singleton

/**
 * @author boris
 * @created 2022/01/19
 */
@InstallIn(SingletonComponent::class)
@Module
object Request {
    @Provides
    @Singleton
    fun requestBuild(): Request {
        return Request.Builder().url("ws://127.0.0.1:8080/app/pub/chat/room/")
            .build()
    }
}
