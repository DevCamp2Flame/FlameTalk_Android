package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/14
 */
class GetChatRoomModelUseCase @Inject constructor(
    val repository: ChatRoomRepository
) {
    fun invoke(chatroomId: String): Flow<LocalResults<ChatRoom>> {
        return repository.getChatRoomModel(chatroomId)
    }
}
