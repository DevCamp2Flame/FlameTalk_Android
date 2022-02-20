package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/27
 */
class GetChatRoomListUseCase @Inject constructor(
    private val repository: ChatRoomRepository
) {
    fun invoke(isOpen: Boolean): Flow<Results<GetChatRoomListRes, WrappedResponse<GetChatRoomListRes>>> {
        return repository.getChatRoomList(isOpen)
    }
}
