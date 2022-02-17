package com.sgs.devcamp2.flametalk_android.data.repository

import android.util.Log
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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/31
 * Domain layer의 Chatrespository의 구현체
 */
class ChatRepositoryImp @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val db: AppDatabase,
    private val remote: ChatApi
) : ChatRepository {
    /**
     * Websocket으로 수신받은 Message를 Local Database에 저장하는 function입니다.
     * @param chatRes 채팅 수신 response
     * @desc 채팅을 수신하면 chat data model로 chatres를 mapping하고 database에 저장합니다.
     */
    override fun saveReceivedMessage(chatRes: ChatRes): Flow<Long> {
        return flow {
            val chat = mapperToChat(chatRes)
            val index = db.chatDao().insert(chat)
            val chatRoomUpdateModel = mapperToChatRoomUpdateModel(chatRes)
            val updateIndex = db.chatRoomDao().updateLastReadMessageId(chatRoomUpdateModel)
            emit(index)
        }.flowOn(ioDispatcher)
    }
    /**
     * 마지막으로 읽은 메시지 이후에 모든 메시지를 가져오는 function입니다.
     * @param roomId 채팅방id
     * @param lastReadMessage 마지막으로 읽은 메세지 id
     * @desc 채팅 텍스트 리스트를 응답받으며, 받은 응답을 room database에 저장합니다.
     */
    override fun getMessageHistory(
        roomId: String,
        lastReadMessage: String
    ): Flow<Results<List<ChatRes>, WrappedResponse<List<ChatRes>>>> {
        return flow {
            val response = remote.getChatMessageHistory(roomId, lastReadMessage)
            val TAG: String = "로그"
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val data = response.body()!!.data

                    var deferred = coroutineScope {
                        async {
                            try {
                                for (i in 0 until data!!.size) {
                                    val chat = mapperToChat(data[i])
                                    db.chatDao().insert(chat)
                                }
                                Log.d(TAG, "ChatRepositoryImp - getMessageHistory(1) called")
                            } catch (e: Exception) {
                            }
                        }
                    }
                    deferred.await()
                    Log.d(TAG, "ChatRepositoryImp - getMessageHistory(2) called")
                    emit(Results.Success(data!!))
                }
            } else {
                Log.d(TAG, "response - notSuccessful")
            }
        }.flowOn(ioDispatcher)
    }

    override fun getLastReadMessageId(chatroomId: String): Flow<LocalResults<String>> {
        return flow {
            try {
                db.chatRoomDao().getLastReadMessageId(chatroomId)?.collect {
                    emit(LocalResults.Success(it))
                }
            } catch (e: NullPointerException) {
                emit(LocalResults.Error("lastReadMessageId가 없습니다."))
            }
        }.flowOn(ioDispatcher)
    }
}
