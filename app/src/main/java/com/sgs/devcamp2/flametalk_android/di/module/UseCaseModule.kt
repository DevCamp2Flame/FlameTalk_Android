package com.sgs.devcamp2.flametalk_android.di.module


import com.sgs.devcamp2.flametalk_android.data.repository.ChatRoomListRepository
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author boris
 * @created 2022/01/27
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providesGetChatRoomListUseCase(repository: ChatRoomListRepository): GetChatRoomListUseCase {
        return GetChatRoomListUseCase(repository = repository)
    }
}
