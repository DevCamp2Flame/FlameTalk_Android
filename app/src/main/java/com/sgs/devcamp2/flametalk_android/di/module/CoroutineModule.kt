package com.sgs.devcamp2.flametalk_android.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

/**
 * @author 박소연
 * @created 2022/02/09
 * @desc Coroutine을 실행할 범위를 지정해준다.
 */

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher { // 백그라운드에서 실행되도록 지정
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideExternalScope(): CoroutineScope {
        return GlobalScope
    }
}
