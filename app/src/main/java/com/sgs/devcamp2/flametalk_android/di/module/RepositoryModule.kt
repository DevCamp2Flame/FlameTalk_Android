package com.sgs.devcamp2.flametalk_android.di.module

import com.sgs.devcamp2.flametalk_android.data.repository.ChatRoomListRepository
import com.sgs.devcamp2.flametalk_android.data.repository.ChatRoomListRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * @author boris
 * @created 2022/01/28
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideChatRoomListRepository(impl: ChatRoomListRepositoryImp): ChatRoomListRepository
}
