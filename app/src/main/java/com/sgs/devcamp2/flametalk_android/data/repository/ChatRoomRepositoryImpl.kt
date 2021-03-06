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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * @author ?????????
 * @created 2022/02/05
 */
class ChatRoomRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val local: AppDatabase,
    private val remote: ChatRoomApi,
    private val friendRemote: FriendApi,
) : ChatRoomRepository {
    /**
     * ????????? ?????? api ?????? ??? room database ?????? function?????????.
     * @param createChatRoomReq ????????? ?????? request model
     * @desc ????????? ????????? ?????? api??? ????????????, response??? ?????? chatroomModel??? chatRoomEntity??? mapping?????????.
     * chatRoomModel??? room database??? ????????????, chatRoomEntity??? ????????? viewModel??? ???????????? ?????????.
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
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
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
     * ?????? ????????? ????????????  function?????????.
     * @param chatroomId ????????? roomId
     * @desc room Database??? ????????? ?????? ???????????? ???????????????.
     * api??? ?????? local?????? ???????????? ????????? localResults sealed class??? ??????????????????.
     */
    override fun getChatList(chatroomId: String): Flow<LocalResults<ChatWithRoomId>> {
        return flow {
            var chatwithRoomId: ChatWithRoomId? = null
            var deffer: Deferred<ChatWithRoomId> = CoroutineScope(ioDispatcher).async {
                local.chatRoomDao().getChatRoomWithId(chatroomId)
            }
            chatwithRoomId = deffer.await()
            emit(LocalResults.Success(chatwithRoomId))
        }.flowOn(ioDispatcher)
    }
    /**
     * ????????? ???????????? ???????????? function?????????.
     * @param userChatroomId ???????????????Id
     * @desc api ????????? ????????? ??????????????? ???????????? ?????? ?????? ??? ??? ??? ????????????.
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
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else if (response.body()!!.status == 404) {
                    emit(Results.Error("???????????? ?????? ??????????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * ????????? ????????? ???????????? function?????????.
     * @param isOpen ??????????????? ??????
     * @desc ????????? ???????????? ????????????, ?????? database??? ???????????????.
     * ?????? ????????? ???????????? ?????????, ?????? ?????? database??? ????????? ????????? ???????????? ?????????,
     * ????????? ???????????? ???????????? ???????????????.
     */
    override fun getChatRoomList(isOpen: Boolean): Flow<Results<GetChatRoomListRes, WrappedResponse<GetChatRoomListRes>>> {
        return flow {
            val response = remote.getChatRoomList(isOpen)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    for (i in 0 until data.userChatrooms.size) {
                        // local db?????? ???????????? ????????? ?????????.
                        val getChatRoomWithThumnail = local.chatRoomDao()
                            .getChatRoomWithThumbnailAndId(data.userChatrooms[i].chatroomId)
                        if (getChatRoomWithThumnail == null) {
                            // ???????????? ????????? ?????????.
                            val chatRoom = mapperToChatRoomModel(isOpen, i, data)
                            var deferred = coroutineScope {
                                async {
                                    local.chatRoomDao().insert(chatRoom)
                                }
                            }
                            deferred.await()
                            var deferred2 = coroutineScope {
                                async {
                                    if (data.userChatrooms[i].thumbnail?.size != 0) {
                                        for (j in 0 until data.userChatrooms[i].thumbnail!!.size) {
                                            lateinit var thumbnail: Thumbnail
                                            if (data.userChatrooms[i].thumbnail?.get(j)
                                                .isNullOrEmpty()
                                            ) {
                                                thumbnail = mapperToThumbnail(
                                                    data.userChatrooms[i].chatroomId,
                                                    ""
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
                            }
                            deferred2.await()
                        } else {
                            var deffered3 = coroutineScope {
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
                                            lateinit var thumbnail: Thumbnail
                                            if (data.userChatrooms[i].thumbnail?.get(j)
                                                .isNullOrEmpty()
                                            ) {
                                                thumbnail = mapperToThumbnail(
                                                    data.userChatrooms[i].chatroomId,
                                                    ""
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
                            }
                            deffered3.await()
                        }
                    }
                    emit(Results.Success(data))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * ?????? local database??? ????????? chatroomList??? ???????????? function?????????.
     * @param isOpen ??????????????????
     * @desc ???????????? ???????????? ?????? ????????? ???????????? ???????????????.
     */
    override fun getLocalChatRoomList(isOpen: Boolean): Flow<LocalResults<List<ThumbnailWithRoomId>>> {
        return flow {
            local.chatRoomDao().getChatRoom(isOpen).collect {
                emit(LocalResults.Success(it))
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * ???????????? ???????????? ?????? ???????????? ???????????? function?????????.
     * @param chatroomId ?????????id
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
     * ????????? ?????? function?????????.
     * @param userChatroomId ???????????????id
     * @param updateChatRoomReq ???????????? ?????? request model
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
                    local.chatRoomDao()
                        .updateChatRoomTitle(data.title, data.inputLock, data.userChatroomId)
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
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * ??????????????? ????????? ??? lastReadMessageId??? ???????????? function?????????.
     * @param closeChatRoomReq ?????????????????? request model
     */
    override fun closeChatRoom(closeChatRoomReq: CloseChatRoomReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val TAG: String = "??????"
            val response = remote.closeChatRoom(closeChatRoomReq)

            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    var response: String = ""
                    var updateResponse = 0
                    var deffer: Deferred<String> = CoroutineScope(ioDispatcher).async {
                        local.chatDao().getContents(closeChatRoomReq.lastReadMessageId)
                    }
                    response = deffer.await()
                    var deffer2: Deferred<Int> = CoroutineScope(ioDispatcher).async {
                        local.chatRoomDao().updateChatRoomWithMessageText(
                            closeChatRoomReq.lastReadMessageId,
                            response,
                            System.currentTimeMillis(),
                            closeChatRoomReq.userChatroomId,
                        )
                    }
                    updateResponse = deffer2.await()
                    emit(Results.Success(true))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * ??????????????? ???????????? ?????????????????? function?????????.
     * @param updateOpenChatRoomProfileReq ???????????????????????? ???????????? data model
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
     * ???????????? ?????????(????????????) function?????????.
     * @param userChatroomId ???????????????id
     */
    override fun leaveChatRoom(userChatroomId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.leaveChatRoom(userChatroomId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    local.chatRoomDao().deleteChatRoomWithuserChatroomId(userChatroomId)
                    emit(Results.Success(true))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
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
            Log.d("??????", "response - $response() called")
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun joinUser(joinChatRoomReq: JoinChatRoomReq): Flow<Results<JoinChatRoomRes, WrappedResponse<JoinChatRoomRes>>> {
        return flow {
            val response = remote.joinChatRoom(joinChatRoomReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
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
            Log.d("??????", "ChatRoomRepositoryImpl - getFriendUser() called")
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("????????? ???????????????"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("????????? ????????????"))
                } else {
                    emit(Results.Error("?????? ???????????????"))
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
