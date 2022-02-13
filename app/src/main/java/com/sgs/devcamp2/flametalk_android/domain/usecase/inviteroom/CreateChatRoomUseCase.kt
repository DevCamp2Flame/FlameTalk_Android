package com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.ChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/29
 */
class CreateChatRoomUseCase @Inject constructor(
    private val repository: ChatRoomRepository
) {
    suspend fun invoke(createChatRoomReq: CreateChatRoomReq):
        Flow<Results<ChatRoomEntity, WrappedResponse<CreateChatRoomRes>>> {
        return repository.createChatRoom(createChatRoomReq)
    }
}
