package com.sgs.devcamp2.flametalk_android.di.module

import com.sgs.devcamp2.flametalk_android.data.repository.*
import com.sgs.devcamp2.flametalk_android.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author 김현국
 * @created 2022/01/28
 * data layer의 impl class 를 respository에 바인딩
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideMainActivityRepository(impl: MainActivityRepositoryImpl): MainActivityRepository

    @Binds
    fun provideOpenProfileRepository(impl: OpenProfileRepositoryImpl): OpenProfileRepository

    @Binds
    fun provideChatRepository(impl: ChatRepositoryImp): ChatRepository

    @Binds
    fun provideChatRoomApiRepository(impl: ChatRoomRepositoryImpl): ChatRoomRepository

    @Binds
    fun provideDeviceApiRepository(impl: DeviceRepositoryImpl): DeviceRepository

    @Binds
    fun provideAuthApiRepository(impl: AuthRepositoryImpl): AuthRepository
}
