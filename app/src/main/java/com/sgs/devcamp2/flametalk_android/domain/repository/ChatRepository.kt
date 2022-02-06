package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/31
 */
interface ChatRepository {
    suspend fun saveReceivedMessage(chatEntity: ChatEntity): Flow<Long>
    suspend fun savePushMessage(chatEntity: ChatEntity): Flow<Long>
}
