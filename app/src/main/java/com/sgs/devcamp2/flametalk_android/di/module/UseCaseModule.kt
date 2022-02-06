package com.sgs.devcamp2.flametalk_android.di.module

import com.sgs.devcamp2.flametalk_android.domain.repository.*
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.createopenchatprofile.CreateOpenProfileUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.SaveReceivedMessageUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenchatprofile.GetOpenProfileListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail.DeleteOpenProfileUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail.GetOpenProfileUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail.UpdateOpenProfileUseCase
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
    fun providesGetChatRoomListUseCase(repository: ChatRoomRepository): GetChatRoomListUseCase {
        return GetChatRoomListUseCase(repository = repository)
    }
    @Provides
    fun provideCreateChatRoomUseCase(repository: ChatRoomRepository): CreateChatRoomUseCase {
        return CreateChatRoomUseCase(repository = repository)
    }
    @Provides
    fun provideCreateOpenChatProfileUseCase(repository: OpenProfileRepository): CreateOpenProfileUseCase {
        return CreateOpenProfileUseCase(repository = repository)
    }
    @Provides
    fun provideGetOpenProfileListUseCase(repository: OpenProfileRepository): GetOpenProfileListUseCase {
        return GetOpenProfileListUseCase(repository = repository)
    }
    @Provides
    fun provideGetOpenProfileUseCase(repository: OpenProfileRepository): GetOpenProfileUseCase {
        return GetOpenProfileUseCase(repository = repository)
    }
    @Provides
    fun provideSaveReceivedMessageUseCase(repository: ChatRepository): SaveReceivedMessageUseCase {
        return SaveReceivedMessageUseCase(repository = repository)
    }
    @Provides
    fun provideUpdateOpenProfileUseCase(repository: OpenProfileRepository): UpdateOpenProfileUseCase {
        return UpdateOpenProfileUseCase(repository = repository)
    }
    @Provides
    fun provideDeleteOpenProfileUseCase(repository: OpenProfileRepository): DeleteOpenProfileUseCase {
        return DeleteOpenProfileUseCase(repository = repository)
    }
}
