package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.common.Status
import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChatRoomEntity
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChatRoomModel
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToGetChatRoomEntity
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToThumbnail
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
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.inviteroom.FriendListRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatroom.JoinChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatroom.JoinChatRoomRes
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/05
 */
class ChatRoomRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val local: AppDatabase,
    private val remote: ChatRoomApi,
    private val friendRemote: FriendApi,
) : ChatRoomRepository {
    /**
     * 채팅방 생성 api 호출 및 room database 저장 function 입니다.
     * @param createChatRoomReq 채팅방 생성 request model
     * @desc 서버에 채팅방 생성 api 를 호출하고, response 를 받아 chatroomModel 과 chatRoomEntity 로 mapping 합니다.
     * chatRoomModel 를 room database 에 저장하고, chatRoomEntity 는 호출된 viewModel 로 이동하게 됩니다.
     */
    override fun createChatRoom(createChatRoomReq: CreateChatRoomReq): Flow<Results<ChatRoomEntity, WrappedResponse<CreateChatRoomRes>>> {
        return flow {
            val response = remote.createChatRoom(createChatRoomReq)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val body = response.body()!!
                        val data = body.data!!
                        val chatroomModel = mapperToChatRoomModel(data)
                        local.chatRoomDao().insert(chatroomModel)
                        val chatRoomEntity = mapperToChatRoomEntity(data)
                        emit(Results.Success(chatRoomEntity))
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
    override fun getUserImage(userId: String): Flow<LocalResults<String>> {
        return flow {
            val response = local.friendDao().getFriendImageUrl(userId)
            emit(LocalResults.Success(response))
        }
    }

    override fun enterChatRoom() {
        TODO("Not yet implemented")
    }
    /**
     * 채팅 리스트 불러오기  function 입니다.
     * @param chatroomId 채팅방 roomId
     * @desc room Database 에 저장된 채팅 텍스트를 가져옵니다.
     * api 가 아님 local 에서 불러오기 때문에 localResults sealed class 를 사용했습니다.
     */
    override fun getChatList(chatroomId: String): Flow<LocalResults<ChatWithRoomId>> {
        return flow {
            val chatWithRoomId: ChatWithRoomId?
            val deffer: Deferred<ChatWithRoomId> = CoroutineScope(ioDispatcher).async {
                local.chatRoomDao().getChatRoomWithId(chatroomId)
            }
            chatWithRoomId = deffer.await()
            emit(LocalResults.Success(chatWithRoomId))
        }.flowOn(ioDispatcher)
    }
    /**
     * 채팅의 상세정보 불러오기 function 입니다.
     * @param userChatroomId 유저채팅방Id
     * @desc api 호출을 통해서 보고자하는 채팅방의 상세 정보 를 볼 수 있습니다.
     */
    override fun getChatRoomInfo(userChatroomId: Long): Flow<Results<GetChatRoomEntity, WrappedResponse<GetChatRoomRes>>> {
        return flow {
            val response = remote.getChatRoom(userChatroomId)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val body = response.body()!!
                        val data = body.data!!
                        val getChatRoomEntity = mapperToGetChatRoomEntity(data)
                        emit(Results.Success(getChatRoomEntity))
                    }
                    Status.BAD_REQUEST -> {
                        emit(Results.Error("잘못된 요청입니다"))
                    }
                    Status.UNAUTHORIZED -> {
                        emit(Results.Error("권한이 없습니다"))
                    }
                    Status.FILE_NOT_FOUND -> {
                        emit(Results.Error("존재하지 않는 채팅방입니다"))
                    }
                    else -> {
                        emit(Results.Error("서버 에러입니다"))
                    }
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 채팅방 리스트 불러오기 function 입니다.
     * @param isOpen 오픈채팅방 유무
     * @desc 채팅방 리스트를 불러오고, 내부 database 에 저장합니다.
     * 또한 변경된 썸네일이 있다면, 기존 내부 database 에 저장된 썸네일 리스트를 지우고,
     * 새로운 썸네일로 변경하고 저장합니다.
     */
    override fun getChatRoomList
    (isOpen: Boolean): Flow<Results<GetChatRoomListRes, WrappedResponse<GetChatRoomListRes>>> {
        return flow {
            val response = remote.getChatRoomList(isOpen)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val body = response.body()!!
                        val data = body.data!!
                        for (i in 0 until data.userChatrooms.size) {
                            // local db 에서 데이터가 있는지 찾는다.
                            local.chatRoomDao().getChatRoomWithThumbnailAndId(data.userChatrooms[i].chatroomId)
                            val deferred = coroutineScope {
                                async {
                                    local.chatRoomDao().updateChatRoomInfo(
                                        data.userChatrooms[i].title,
                                        data.userChatrooms[i].inputLock,
                                        data.userChatrooms[i].count,
                                        data.userChatrooms[i].chatroomId
                                    )
                                    if (data.userChatrooms[i].thumbnail?.size != 0) {
                                        local.chatRoomDao()
                                            .deleteThumbnailwithRoomId(data.userChatrooms[i].chatroomId)
                                        for (j in 0 until data.userChatrooms[i].thumbnail!!.size) {
                                            val thumbnail: Thumbnail = if (data.userChatrooms[i].thumbnail?.get(j)
                                                .isNullOrEmpty()
                                            ) {
                                                mapperToThumbnail(
                                                    data.userChatrooms[i].chatroomId,
                                                    ""
                                                )
                                            } else {
                                                mapperToThumbnail(
                                                    data.userChatrooms[i].chatroomId,
                                                    data.userChatrooms[i].thumbnail?.get(j)!!
                                                )
                                            }
                                            local.chatRoomDao().insertThumbnail(thumbnail)
                                        }
                                    }
                                }
                            }
                            deferred.await()
                        }
                        emit(Results.Success(data))
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
    /**
     * 내부 local database 에 저장된 chatroomList 를 가져오는 function 입니다.
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
     * 채팅방에 업로드된 파일 리스트를 불러오는 function 입니다.
     * @param chatroomId 채팅룸 id
     */
    override fun getChatRoomFileList(chatroomId: String): Flow<Results<List<GetChatRoomFilesRes>, WrappedResponse<List<GetChatRoomFilesRes>>>> {
        return flow {
            val response = remote.getChatRoomFileList(chatroomId)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val body = response.body()!!
                        val data = body.data!!
                        emit(Results.Success(data))
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
    /**
     * 채팅방 수정 function 입니다.
     * @param userChatroomId 유저 채팅방 id
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
                when (response.body()!!.status) {
                    200 -> {
                        val body = response.body()!!
                        val data = body.data!!
                        local.chatRoomDao()
                            .updateChatRoomTitle(data.title, data.inputLock, data.userChatroomId)
                        local.chatRoomDao().deleteThumbnailwithRoomId(chatroomId)
                        val thumbnail: Thumbnail = mapperToThumbnail(
                            chatroomId,
                            data.thumbnail[0]
                        )
                        local.chatRoomDao().insertThumbnail(thumbnail)

                        emit(Results.Success(data))
                    }
                    400 -> {
                        emit(Results.Error("잘못된 요청입니다"))
                    }
                    401 -> {
                        emit(Results.Error("권한이 없습니다"))
                    }
                    else -> {
                        emit(Results.Error("서버 에러입니다"))
                    }
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 채팅방에서 벗어날 때 lastReadMessageId를 갱신하는 function 입니다.
     * @param closeChatRoomReq 채팅방나가기 request model
     */
    override fun closeChatRoom(closeChatRoomReq: CloseChatRoomReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.closeChatRoom(closeChatRoomReq)

            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val res: String
                        val deffer: Deferred<String> = CoroutineScope(ioDispatcher).async {
                            local.chatDao().getContents(closeChatRoomReq.lastReadMessageId)
                        }
                        res = deffer.await()
                        val deffer2: Deferred<Int> = CoroutineScope(ioDispatcher).async {
                            local.chatRoomDao().updateChatRoomWithMessageText(
                                closeChatRoomReq.lastReadMessageId,
                                res,
                                System.currentTimeMillis(),
                                closeChatRoomReq.userChatroomId,
                            )
                        }
                        deffer2.await()
                        emit(Results.Success(true))
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
    /**
     * 오픈채팅방 프로필을 업데이트하는 function 입니다.
     * @param updateOpenChatRoomProfileReq 오픈채팅방프로필 업데이트 data model
     */
    override fun updateOpenChatRoomProfile(updateOpenChatRoomProfileReq: UpdateOpenChatRoomProfileReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.updateOpenChatRoomProfile(updateOpenChatRoomProfileReq)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        emit(Results.Success(true))
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
    /**
     * 채팅방을 나가는(삭제하는) function 입니다.
     * @param userChatroomId 유저채팅방 id
     */
    override fun leaveChatRoom(userChatroomId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.leaveChatRoom(userChatroomId)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        local.chatRoomDao().deleteChatRoomWithuserChatroomId(userChatroomId)
                        emit(Results.Success(true))
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

    override fun uploadImage(
        file: MultipartBody.Part,
        chatroomId: RequestBody?
    ): Flow<Results<UploadImgRes, WrappedResponse<UploadImgRes>>> {
        return flow {
            val response = remote.fileCreate(file, chatroomId)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val body = response.body()!!
                        val data = body.data!!
                        emit(Results.Success(data))
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

    override fun joinUser(joinChatRoomReq: JoinChatRoomReq): Flow<Results<JoinChatRoomRes, WrappedResponse<JoinChatRoomRes>>> {
        return flow {
            val response = remote.joinChatRoom(joinChatRoomReq)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val body = response.body()!!
                        val data = body.data!!
                        emit(Results.Success(data))
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

    override fun getFriendUser(
        isBirth: Boolean,
        isHidden: Boolean,
        isBlock: Boolean
    ): Flow<Results<List<FriendListRes>, WrappedResponse<List<FriendListRes>>>> {
        return flow {
            val response = friendRemote.getFriendList(isBirth, isHidden, isBlock)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val body = response.body()!!
                        val data = body.data!!
                        emit(Results.Success(data))
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
    override fun getUserChatRoomId(roomId: String): Flow<LocalResults<Long>> {
        return flow {
            val response = local.chatRoomDao().getUserChatRoomId(roomId)
            emit(LocalResults.Success(response))
        }.flowOn(ioDispatcher)
    }

    override fun updateChatRoomText(text: String, roomId: String): Flow<LocalResults<Boolean>> {
        return flow {
            var response = 0
            val deffer = coroutineScope {
                async {
                    response = local.chatRoomDao().updateText(text, roomId, System.currentTimeMillis())
                }
            }
            deffer.await()
            if (response != 0) {
                emit(LocalResults.Success(true))
            } else {
                emit(LocalResults.Success(false))
            }
        }.flowOn(ioDispatcher)
    }
}
