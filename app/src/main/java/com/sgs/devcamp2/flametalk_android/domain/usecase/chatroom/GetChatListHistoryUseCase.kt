package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/13
 */
class GetChatListHistoryUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun invoke(roomId: String, lastReadMessageId: String): Flow<Results<List<ChatRes>, WrappedResponse<List<ChatRes>>>> {
        return repository.getMessageHistory(roomId, lastReadMessageId)
    }
}
