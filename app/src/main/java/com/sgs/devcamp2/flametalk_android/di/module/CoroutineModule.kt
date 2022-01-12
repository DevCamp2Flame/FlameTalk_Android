package com.sgs.devcamp2.flametalk_android.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideExternalScope(): CoroutineScope {
        return GlobalScope
    }
}