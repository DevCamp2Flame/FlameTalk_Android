package com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.CreateChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/29
 */
class CreateChatRoomUseCase @Inject constructor(
    private val repository: ChatRoomRepository
) {
    suspend fun invoke(createChatRoomReq: CreateChatRoomReq):
        Flow<Results<CreateChatRoomEntity, WrappedResponse<CreateChatRoomRes>>> {
        return repository.createChatRoom(createChatRoomReq)
    }
}
