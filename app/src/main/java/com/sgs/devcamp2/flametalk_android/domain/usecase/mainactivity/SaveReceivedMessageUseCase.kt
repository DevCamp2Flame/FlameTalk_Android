package com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity

import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/04
 */
class SaveReceivedMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend fun invoke(chat: Chat): Flow<Long> {
        return repository.saveReceivedMessage(chat)
    }
}
