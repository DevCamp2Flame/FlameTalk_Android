package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChat
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChatRoomUpdateModel
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
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
    override fun saveReceivedMessage(chatRes: ChatRes): Flow<Long> {
        return flow {
            val chat = mapperToChat(chatRes)
            val index = db.chatDao().insert(chat)
            val chatRoomUpdateModel = mapperToChatRoomUpdateModel(chatRes)
            val updateIndex = db.chatRoomDao().updateLastReadMessageId(chatRoomUpdateModel)
            emit(index)
        }.flowOn(ioDispatcher)
    }
}
