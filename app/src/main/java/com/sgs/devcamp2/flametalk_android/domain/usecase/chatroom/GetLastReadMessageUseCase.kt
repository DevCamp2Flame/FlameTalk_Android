package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/14
 */
class GetLastReadMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend fun invoke(chatroomId: String): Flow<LocalResults<String>> {
        return repository.getLastReadMessageId(chatroomId)
    }
}
