package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatWithRoomId
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/08
 */
class GetChatListUseCase @Inject constructor(
    private val repository: ChatRoomRepository
) {
    suspend fun invoke(chatroomId: String): Flow<LocalResults<ChatWithRoomId>> {
        return repository.getChatList(chatroomId)
    }
}
