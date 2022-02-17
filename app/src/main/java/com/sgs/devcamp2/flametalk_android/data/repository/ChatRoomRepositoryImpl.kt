package com.sgs.devcamp2.flametalk_android.data.repository

import android.util.Log
import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.mapper.*
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.closechatroom.CloseChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroom.GetChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomfiles.GetChatRoomFilesRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatrom.FriendListRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatrom.JoinChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatrom.JoinChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updateopenchatroomprofile.UpdateOpenChatRoomProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.uploadimg.UploadImgRes
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.ChatRoomApi
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.FriendApi
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.ChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.GetChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.NullPointerException
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/05
 */
class ChatRoomRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val local: AppDatabase,
    private val remote: ChatRoomApi,
    private val friendRemote: FriendApi
) : ChatRoomRepository {
    /**
     * 채팅방 생성 api 호출 및 room database 저장 function입니다.
     * @param createChatRoomReq 채팅방 생성 request model
     * @desc 서버에 채팅방 생성 api를 호출하고, response를 받아 chatroomModel과 chatRoomEntity로 mapping합니다.
     * chatRoomModel를 room database에 저장하고, chatRoomEntity는 호출된 viewModel로 이동하게 됩니다.
     */
    override fun createChatRoom(createChatRoomReq: CreateChatRoomReq): Flow<Results<ChatRoomEntity, WrappedResponse<CreateChatRoomRes>>> {
        return flow {
            val response = remote.createChatRoom(createChatRoomReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    val chatroomModel = mapperToChatRoomModel(data)
                    local.chatRoomDao().insert(chatroomModel)
                    val chatRoomEntity = mapperToChatRoomEntity(data)
                    emit(Results.Success(chatRoomEntity))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun enterChatRoom() {
        TODO("Not yet implemented")
    }
    /**
     * 채팅 리스트 불러오기  function입니다.
     * @param chatroomId 채팅방 roomId
     * @desc room Database에 저장된 채팅 텍스트를 가져옵니다.
     * api가 아님 local에서 불러오기 때문에 localResults sealed class를 사용했습니다.
     */
    override fun getChatList(chatroomId: String): Flow<LocalResults<ChatWithRoomId>> {
        return flow {
            val TAG: String = "로그"
            Log.d(TAG, "chatroomId - $chatroomId() called")
            local.chatRoomDao().getChatRoomWithId(chatroomId).collect {
                try {
                    if (it != null) {
                        Log.d(TAG, "ChatRoomRepositoryImpl getChatList - $it() called")
                        if (it.room.id == chatroomId) {
                            emit(LocalResults.Success(it))
                        }
                    }
                } catch (e: NullPointerException) {
                    Log.d(TAG, "error - ${e.cause}")
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 채팅의 상세정보 불러오기 function입니다.
     * @param userChatroomId 유저채팅방Id
     * @desc api 호출을 통해서 보고자하는 채팅방의 상세 정보 를 볼 수 있습니다.
     */
    override fun getChatRoomInfo(userChatroomId: Long): Flow<Results<GetChatRoomEntity, WrappedResponse<GetChatRoomRes>>> {
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
    /**
     * 채팅방 리스트 불러오기 function입니다.
     * @param isOpen 오픈채팅방 유무
     * @desc 채팅방 리스트를 불러오고, 내부 database에 저장합니다.
     * 또한 변경된 썸네일이 있다면, 기존 내부 database에 저장된 썸네일 리스트를 지우고,
     * 새로운 썸네일로 변경하고 저장합니다.
     */
    override fun getChatRoomList(isOpen: Boolean): Flow<Results<GetChatRoomListRes, WrappedResponse<GetChatRoomListRes>>> {
        return flow {
            val response = remote.getChatRoomList(isOpen)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    for (i in 0 until data.userChatrooms.size) {
                        val chatRoom = mapperToChatRoomModel(isOpen, i, data)
                        local.chatRoomDao().insert(chatRoom)
                        if (data.userChatrooms[i].thumbnail?.size == 0) {
                        } else {
                            local.chatRoomDao().deleteThumbnailwithRoomId(chatRoom.id)
                            for (j in 0 until data.userChatrooms[i].thumbnail!!.size) {
                                lateinit var thumbnail: Thumbnail
                                if (data.userChatrooms[i].thumbnail?.get(j).isNullOrEmpty()) {
                                    thumbnail = mapperToThumbnail(
                                        data.userChatrooms[i].chatroomId,
                                        "https://cdn.pixabay.com/photo/2017/02/15/12/12/cat-2068462__480.jpg"
                                    )
                                } else {
                                    thumbnail = mapperToThumbnail(
                                        data.userChatrooms[i].chatroomId,
                                        data.userChatrooms[i].thumbnail?.get(j)!!
                                    )
                                }
                                local.chatRoomDao().insertThumbnail(thumbnail)
                            }
                        }
                    }
                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 내부 local database에 저장된 chatroomList를 가져오는 function입니다.
     * @param isOpen 오픈채팅유무
     * @desc 채팅방의 썸네일과 함께 채팅룸 리스트를 반환합니다.
     */
    override fun getLocalChatRoomList(isOpen: Boolean): Flow<LocalResults<List<ThumbnailWithRoomId>>> {
        return flow {
            local.chatRoomDao().getChatRoom(isOpen).collect {
                emit(LocalResults.Success(it))
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 채팅방에 업로드된 파일 리스트를 불러오는 function입니다.
     * @param chatroomId 채팅룸id
     */
    override fun getChatRoomFileList(chatroomId: String): Flow<Results<List<GetChatRoomFilesRes>, WrappedResponse<List<GetChatRoomFilesRes>>>> {
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
    /**
     * 채팅방 수정 function입니다.
     * @param userChatroomId 유저채팅방id
     * @param updateChatRoomReq 업데이트 요청 request model
     */
    override fun updateChatRoom(
        chatroomId: String,
        userChatroomId: Long,
        updateChatRoomReq: UpdateChatRoomReq
    ): Flow<Results<UpdateChatRoomRes, WrappedResponse<UpdateChatRoomRes>>> {
        return flow {
            val response = remote.updateChatRoom(userChatroomId, updateChatRoomReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    local.chatRoomDao().updateChatRoomTitle(data.title, data.inputLock, data.userChatroomId)
                    if (data.thumbnail[0] != null) {
                        local.chatRoomDao().deleteThumbnailwithRoomId(chatroomId)
                        lateinit var thumbnail: Thumbnail
                        thumbnail = mapperToThumbnail(
                            chatroomId,
                            data.thumbnail[0]
                        )
                        local.chatRoomDao().insertThumbnail(thumbnail)
                    }

                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 채팅방에서 벗어날 때 lastReadMessageId를 갱신하는 function입니다.
     * @param closeChatRoomReq 채팅방나가기 request model
     */
    override fun closeChatRoom(closeChatRoomReq: CloseChatRoomReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.closeChatRoom(closeChatRoomReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    emit(Results.Success(true))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 오픈채팅방 프로필을 업데이트하는 function입니다.
     * @param updateOpenChatRoomProfileReq 오픈채팅방프로필 업데이트 data model
     */
    override fun updateOpenChatRoomProfile(updateOpenChatRoomProfileReq: UpdateOpenChatRoomProfileReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.updateOpenChatRoomProfile(updateOpenChatRoomProfileReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    emit(Results.Success(true))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 채팅방을 나가는(삭제하는) function입니다.
     * @param userChatroomId 유저채팅방id
     */
    override fun leaveChatRoom(userChatroomId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.leaveChatRoom(userChatroomId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    local.chatRoomDao().deleteChatRoomWithuserChatroomId(userChatroomId)
                    emit(Results.Success(true))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun getThumbnailList(chatroomId: String): Flow<LocalResults<ThumbnailWithRoomId>> {
        return flow<LocalResults<ThumbnailWithRoomId>> {
            local.chatRoomDao().getThumbnailWithRoomId(chatroomId).collect {
                emit(LocalResults.Success(it))
            }
        }.flowOn(ioDispatcher)
    }
    override fun getChatRoomModel(chatroomId: String): Flow<LocalResults<ChatRoom>> {
        return flow {
            local.chatRoomDao().getChatRoomModel(chatroomId).collect {
                emit(LocalResults.Success(it))
            }
        }
    }
    override fun uploadImage(file: MultipartBody.Part, chatroomId: RequestBody?): Flow<Results<UploadImgRes, WrappedResponse<UploadImgRes>>> {
        return flow {
            val response = remote.fileCreate(file, chatroomId)
            Log.d("로그", "response - $response() called")
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                }
            }
        }
    }

    override fun joinUser(joinChatRoomReq: JoinChatRoomReq): Flow<Results<JoinChatRoomRes, WrappedResponse<JoinChatRoomRes>>> {
        return flow {
            val response = remote.joinChatRoom(joinChatRoomReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 201) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else {
                    Log.d("로그", "ChatRoomRepositoryImpl - joinUser() else called")
                }
            }
        }
    }
    override fun getFriendUser(isBirth: Boolean, isHidden: Boolean, isBlock: Boolean): Flow<Results<List<FriendListRes>, WrappedResponse<List<FriendListRes>>>> {
        return flow {
            val response = friendRemote.getFriendList(isBirth, isHidden, isBlock)
            Log.d("로그", "ChatRoomRepositoryImpl - getFriendUser() called")
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else {
                    Log.d("로그", "getFriendUser - error")
                }
            } else {
                Log.d("로그", "ChatRoomRepositoryImpl - not succeeful() called")
            }
        }
    }
}
