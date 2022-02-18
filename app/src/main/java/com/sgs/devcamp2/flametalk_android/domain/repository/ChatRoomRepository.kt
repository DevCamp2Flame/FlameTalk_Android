package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
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
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.ChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.GetChatRoomEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * @author 김현국
 * @created 2022/01/27
 * usecase에서 사용될 respository 실제 구현체는 data layer에 존재
 */
interface ChatRoomRepository {

    fun createChatRoom(createChatRoomReq: CreateChatRoomReq): Flow<Results<ChatRoomEntity, WrappedResponse<CreateChatRoomRes>>>
    fun enterChatRoom()
    fun getChatRoomInfo(userChatroomId: Long): Flow<Results<GetChatRoomEntity, WrappedResponse<GetChatRoomRes>>>
    fun getChatRoomList(isOpen: Boolean): Flow<Results<GetChatRoomListRes, WrappedResponse<GetChatRoomListRes>>>
    fun getLocalChatRoomList(isOpen: Boolean): Flow<LocalResults<List<ThumbnailWithRoomId>>>
    fun getChatRoomFileList(chatroomId: String): Flow<Results<List<GetChatRoomFilesRes>, WrappedResponse<List<GetChatRoomFilesRes>>>>
    fun updateChatRoom(chatroomId: String, userChatroomId: Long, updateChatRoomReq: UpdateChatRoomReq): Flow<Results<UpdateChatRoomRes, WrappedResponse<UpdateChatRoomRes>>>
    fun closeChatRoom(closeChatRoomReq: CloseChatRoomReq): Flow<Results<Boolean, WrappedResponse<Nothing>>>
    fun updateOpenChatRoomProfile(updateOpenChatRoomProfileReq: UpdateOpenChatRoomProfileReq): Flow<Results<Boolean, WrappedResponse<Nothing>>>
    fun leaveChatRoom(userChatroomId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>>
    fun getChatList(chatroomId: String): Flow<LocalResults<ChatWithRoomId>>
    fun getThumbnailList(chatroomId: String): Flow<LocalResults<ThumbnailWithRoomId>>
    fun getChatRoomModel(chatroomId: String): Flow<LocalResults<ChatRoom>>
    fun uploadImage(file: MultipartBody.Part, chatroomId: RequestBody?): Flow<Results<UploadImgRes, WrappedResponse<UploadImgRes>>>
    fun joinUser(joinChatRoomReq: JoinChatRoomReq): Flow<Results<JoinChatRoomRes, WrappedResponse<JoinChatRoomRes>>>
    fun getFriendUser(isBirth: Boolean, isHidden: Boolean, isBlock: Boolean): Flow<Results<List<FriendListRes>, WrappedResponse<List<FriendListRes>>>>
    fun getUserImage(userId: String): Flow<LocalResults<String>>
    fun getUserChatRoomId(roomId: String): Flow<LocalResults<Long>>
}
