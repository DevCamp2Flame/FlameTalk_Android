package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/31
 */
interface ChatRepository {
    suspend fun saveReceivedMessage(chat: Chat): Flow<Long>
    fun savePushMessage(chatEntity: ChatEntity)
}
