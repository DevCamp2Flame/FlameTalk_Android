package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.common.Status
import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChat
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChatRoomUpdateModel
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.ChatApi
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/31
 * Domain layer 의 Chat repository 의 구현체
 */
class ChatRepositoryImp @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val db: AppDatabase,
    private val remote: ChatApi
) : ChatRepository {
    /**
     * WebSocket 으로 수신받은 Message 를 Local Database 에 저장하는 function 입니다.
     * @param chatRes 채팅 수신 response
     * @desc 채팅을 수신하면 chat data model 로 ChatRes 를 mapping 하고 database 에 저장합니다.
     */
    override fun saveReceivedMessage(chatRes: ChatRes): Flow<Long> {
        return flow {
            val chat = mapperToChat(chatRes)
            val deferred: Deferred<Long> = CoroutineScope(ioDispatcher).async {
                db.chatDao().insert(chat)
            }
            val index = deferred.await()
            val chatRoomUpdateModel = mapperToChatRoomUpdateModel(chatRes)
            val deferred2: Deferred<Unit> = CoroutineScope(ioDispatcher).async {
                db.chatRoomDao().updateLastReadMessageId(chatRoomUpdateModel)
            }
            deferred2.await()
            emit(index)
        }.flowOn(ioDispatcher)
    }
    /**
     * 마지막으로 읽은 메시지 이후에 모든 메시지를 가져오는 function 입니다.
     * @param roomId 채팅방 id
     * @param lastReadMessage 마지막으로 읽은 메세지 id
     * @desc 채팅 텍스트 리스트를 응답받으며, 받은 응답을 room database 에 저장합니다.
     */
    override fun getMessageHistory(
        roomId: String,
        lastReadMessage: String
    ): Flow<Results<List<ChatRes>, WrappedResponse<List<ChatRes>>>> {
        return flow {
            val response = remote.getChatMessageHistory(roomId, lastReadMessage)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val data = response.body()!!.data
                        val deferred = coroutineScope {
                            async {
                                try {
                                    for (i in 0 until data!!.size) {
                                        val chat = mapperToChat(data[i])
                                        db.chatDao().insert(chat)
                                    }
                                } catch (e: Exception) {
                                }
                            }
                        }
                        deferred.await()
                        val deferred2: Deferred<Int> = CoroutineScope(ioDispatcher).async {
                            db.chatRoomDao().updateMessageCount(roomId)
                        }
                        deferred2.await()
                        emit(Results.Success(data!!))
                    }
                    Status.BAD_REQUEST -> {
                        emit(Results.Error("잘못된 요청입니다"))
                    }
                    Status.UNAUTHORIZED -> {
                        emit(Results.Error("권한이 없습니다"))
                    }
                    else -> {
                        emit(Results.Error("서버 에러입니다"))
                    }
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun getLastReadMessageId(chatroomId: String): Flow<LocalResults<String>?> {
        return flow {
            val messageId: String
            val deferred: Deferred<String?> = CoroutineScope(ioDispatcher).async {
                db.chatDao().getLastMessageWithRoomId(chatroomId)
            }
            messageId = deferred.await().toString()
            if (messageId != "") {
                emit(LocalResults.Success(messageId))
            } else {
                emit(LocalResults.Error(""))
            }
        }.flowOn(ioDispatcher)
    }
}
