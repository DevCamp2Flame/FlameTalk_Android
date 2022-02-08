package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChatModel
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToCreateChatRoomEntity
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToGetChatRoomEntity
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToThumbnail
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.closechatroom.CloseChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroom.GetChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomfiles.GetChatRoomFilesRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updateopenchatroomprofile.UpdateOpenChatRoomProfileReq
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.ChatRoomApi
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.CreateChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.GetChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/05
 */
class ChatRoomRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val local: AppDatabase,
    private val remote: ChatRoomApi
) : ChatRoomRepository {
    override suspend fun createChatRoom(createChatRoomReq: CreateChatRoomReq): Flow<Results<CreateChatRoomEntity, WrappedResponse<CreateChatRoomRes>>> {
        return flow {
            val response = remote.createChatRoom(createChatRoomReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    val createChatRoomEntity = mapperToCreateChatRoomEntity(data)
                    emit(Results.Success(createChatRoomEntity))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun enterChatRoom() {
        TODO("Not yet implemented")
    }

    override suspend fun getChatList(chatroomId: String): Flow<LocalResults<ChatWithRoomId>> {
        return flow {
            local.chatRoomDao().getChatRoomWithId(chatroomId).collect {
                emit(LocalResults.Success(it))
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun getChatRoom(userChatroomId: Long): Flow<Results<GetChatRoomEntity, WrappedResponse<GetChatRoomRes>>> {
        return flow {
            val response = remote.getChatRoom(userChatroomId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    val getChatRoomEntity = mapperToGetChatRoomEntity(data)
                    emit(Results.Success(getChatRoomEntity))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun getChatRoomList(isOpen: Boolean): Flow<Results<GetChatRoomListRes, WrappedResponse<GetChatRoomListRes>>> {
        return flow {
            val response = remote.getChatRoomList(isOpen)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    for (i in 0 until data.userChatrooms.size) {
                        val chatRoom = mapperToChatModel(i, data)
                        local.chatRoomDao().insert(chatRoom)
                        for (j in 0 until data.userChatrooms[i].thumbnail.size) {
                            val thumbnail = mapperToThumbnail(data.userChatrooms[i].chatroomId, data.userChatrooms[i].thumbnail[j])
                            local.chatRoomDao().insertThumbnail(thumbnail)
                        }
                    }
                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun getChatRoomFileList(chatroomId: String): Flow<Results<List<GetChatRoomFilesRes>, WrappedResponse<List<GetChatRoomFilesRes>>>> {
        return flow {
            val response = remote.getChatRoomFileList(chatroomId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun updateChatRoom(userChatroomId: Long, updateChatRoomReq: UpdateChatRoomReq): Flow<Results<UpdateChatRoomRes, WrappedResponse<UpdateChatRoomRes>>> {
        return flow {
            val response = remote.updateChatRoom(userChatroomId, updateChatRoomReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun closeChatRoom(closeChatRoomReq: CloseChatRoomReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.closeChatRoom(closeChatRoomReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    emit(Results.Success(true))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun updateOpenChatRoomProfile(updateOpenChatRoomProfileReq: UpdateOpenChatRoomProfileReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.updateOpenChatRoomProfile(updateOpenChatRoomProfileReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    emit(Results.Success(true))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun leaveChatRoom(userChatroomId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.leaveChatRoom(userChatroomId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    emit(Results.Success(true))
                }
            }
        }.flowOn(ioDispatcher)
    }
}
