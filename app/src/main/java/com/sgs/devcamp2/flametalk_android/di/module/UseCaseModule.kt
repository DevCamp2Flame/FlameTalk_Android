package com.sgs.devcamp2.flametalk_android.di.module

import com.sgs.devcamp2.flametalk_android.domain.repository.*
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.*
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetLocalChatRoomListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.createopenchatprofile.CreateOpenProfileUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.SaveDeviceTokenUseCase
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
 * @author 김현국
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
    @Provides
    fun provideDeleteChatRoomUseCase(repository: ChatRoomRepository): DeleteChatRoomUseCase {
        return DeleteChatRoomUseCase(repository = repository)
    }
    @Provides
    fun provideUpdateChatRoomUseCase(repository: ChatRoomRepository): UpdateChatRoomUseCase {
        return UpdateChatRoomUseCase(repository = repository)
    }
    @Provides
    fun provideCloseChatRoomUseCase(repository: ChatRoomRepository): CloseChatRoomUseCase {
        return CloseChatRoomUseCase(repository = repository)
    }
    @Provides
    fun provideGetChatListUseCase(repository: ChatRoomRepository): GetChatListUseCase {
        return GetChatListUseCase(repository = repository)
    }
    @Provides
    fun provideGetThumbnailListUseCase(repository: ChatRoomRepository): GetThumbnailListUseCase {
        return GetThumbnailListUseCase(repository = repository)
    }
    @Provides
    fun provideGetLocalChatRoomListUseCase(repository: ChatRoomRepository): GetLocalChatRoomListUseCase {
        return GetLocalChatRoomListUseCase(repository = repository)
    }
    @Provides
    fun provideGetChatListHistoryUseCase(repository: ChatRepository): GetChatListHistoryUseCase {
        return GetChatListHistoryUseCase(repository = repository)
    }
    @Provides
    fun provideSaveDeviceTokenUseCase(repository: DeviceRepository): SaveDeviceTokenUseCase {
        return SaveDeviceTokenUseCase(repository = repository)
    }
}
