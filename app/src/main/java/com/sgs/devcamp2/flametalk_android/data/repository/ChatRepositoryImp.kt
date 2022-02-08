package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChat
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/31
 */
class ChatRepositoryImp @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val db: AppDatabase,
) : ChatRepository {
    override suspend fun saveReceivedMessage(chat: Chat): Flow<Long> {
        return flow {
            val index = db.chatDao().insert(chat)
            emit(index)
        }.flowOn(ioDispatcher)
    }
    override fun savePushMessage(chatEntity: ChatEntity) {
        val chat = mapperToChat(chatEntity)
        db.chatDao().insert(chat)
    }
}
