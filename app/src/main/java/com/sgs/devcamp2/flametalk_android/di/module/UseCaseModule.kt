package com.sgs.devcamp2.flametalk_android.di.module

import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomsRepository
import com.sgs.devcamp2.flametalk_android.domain.repository.CreateOpenChatProfileRepository
import com.sgs.devcamp2.flametalk_android.domain.repository.InviteRoomRepository
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.createopenchatprofile.GetUserProfileUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author boris
 * @created 2022/01/27
 * domain layer usecase에 domain layer repository 주입
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun providesGetChatRoomListUseCase(repository: ChatRoomsRepository): GetChatRoomListUseCase {
        return GetChatRoomListUseCase(repository = repository)
    }

    @Provides
    fun provideCreateChatRoomUseCase(repository: InviteRoomRepository): CreateChatRoomUseCase {
        return CreateChatRoomUseCase(repository = repository)
    }

    @Provides
    fun provideGetUserProfileUseCase(repository: CreateOpenChatProfileRepository): GetUserProfileUseCase {
        return GetUserProfileUseCase(repository = repository)
    }
}
