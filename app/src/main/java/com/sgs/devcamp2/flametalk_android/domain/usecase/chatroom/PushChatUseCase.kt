package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRepository
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/08
 */
class PushChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    fun invoke(chatEntity: ChatEntity) {
        return repository.savePushMessage(chatEntity)
    }
}
