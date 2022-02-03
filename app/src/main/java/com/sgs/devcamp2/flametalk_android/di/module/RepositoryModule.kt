package com.sgs.devcamp2.flametalk_android.di.module

import com.sgs.devcamp2.flametalk_android.data.repository.*
import com.sgs.devcamp2.flametalk_android.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author boris
 * @created 2022/01/28
 * repository의 실제 구현체 ( impl class )를 domain layer의 repository에 bind
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideChatRoomListRepository(impl: ChatRoomListRepositoryImp): ChatRoomsRepository

    @Binds
    fun provideInviteRoomRepository(impl: InviteRoomRepositoryImp): InviteRoomRepository

    @Binds
    fun provideMainActivityRepository(impl: MainActivityRepositoryImpl): MainActivityRepository

    @Binds
    fun provideCreateOpenChatProfileRepository(impl: CreateOpenChatProfileRepositoryImpl): CreateOpenChatProfileRepository
}
