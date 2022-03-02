package com.sgs.devcamp2.flametalk_android.domain

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
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.UserChatRoom
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
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.StandardTestDispatcher
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * @author boris
 * @created 2022/03/02
 */
@ExperimentalCoroutinesApi
class FakeChatRoomRepository : ChatRoomRepository {
    override fun createChatRoom(createChatRoomReq: CreateChatRoomReq): Flow<Results<ChatRoomEntity, WrappedResponse<CreateChatRoomRes>>> {
        TODO("Not yet implemented")
    }
    override fun enterChatRoom() {
        TODO("Not yet implemented")
    }
    override fun getChatRoomInfo(userChatroomId: Long): Flow<Results<GetChatRoomEntity, WrappedResponse<GetChatRoomRes>>> {
        TODO("Not yet implemented")
    }
    override fun getChatRoomList(isOpen: Boolean): Flow<Results<GetChatRoomListRes, WrappedResponse<GetChatRoomListRes>>> {
        return flow {
            val userChatRoom = UserChatRoom("1", 1, "test", null, "123", false, 2)
            val userChatRooms = listOf<UserChatRoom>(userChatRoom)
            val getChatRoomListRes = GetChatRoomListRes("1643163512893324414", userChatRooms)
            emit(Results.Success(getChatRoomListRes))
        }.flowOn(StandardTestDispatcher())
    }

    override fun getLocalChatRoomList(isOpen: Boolean): Flow<LocalResults<List<ThumbnailWithRoomId>>> {
        TODO("Not yet implemented")
    }
    override fun getChatRoomFileList(chatroomId: String): Flow<Results<List<GetChatRoomFilesRes>, WrappedResponse<List<GetChatRoomFilesRes>>>> {
        TODO("Not yet implemented")
    }
    override fun updateChatRoom(
        chatroomId: String,
        userChatroomId: Long,
        updateChatRoomReq: UpdateChatRoomReq
    ): Flow<Results<UpdateChatRoomRes, WrappedResponse<UpdateChatRoomRes>>> {
        TODO("Not yet implemented")
    }
    override fun closeChatRoom(closeChatRoomReq: CloseChatRoomReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        TODO("Not yet implemented")
    }
    override fun updateOpenChatRoomProfile(updateOpenChatRoomProfileReq: UpdateOpenChatRoomProfileReq): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        TODO("Not yet implemented")
    }
    override fun leaveChatRoom(userChatroomId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        TODO("Not yet implemented")
    }
    override fun getChatList(chatroomId: String): Flow<LocalResults<ChatWithRoomId>> {
        TODO("Not yet implemented")
    }
    override fun getThumbnailList(chatroomId: String): Flow<LocalResults<ThumbnailWithRoomId>> {
        TODO("Not yet implemented")
    }
    override fun getChatRoomModel(chatroomId: String): Flow<LocalResults<ChatRoom>> {
        TODO("Not yet implemented")
    }
    override fun uploadImage(
        file: MultipartBody.Part,
        chatroomId: RequestBody?
    ): Flow<Results<UploadImgRes, WrappedResponse<UploadImgRes>>> {
        TODO("Not yet implemented")
    }
    override fun joinUser(joinChatRoomReq: JoinChatRoomReq): Flow<Results<JoinChatRoomRes, WrappedResponse<JoinChatRoomRes>>> {
        TODO("Not yet implemented")
    }
    override fun getFriendUser(
        isBirth: Boolean,
        isHidden: Boolean,
        isBlock: Boolean
    ): Flow<Results<List<FriendListRes>, WrappedResponse<List<FriendListRes>>>> {
        TODO("Not yet implemented")
    }
    override fun getUserImage(userId: String): Flow<LocalResults<String>> {
        TODO("Not yet implemented")
    }
    override fun getUserChatRoomId(roomId: String): Flow<LocalResults<Long>> {
        TODO("Not yet implemented")
    }
    override fun updateChatRoomText(text: String, roomId: String): Flow<LocalResults<Boolean>> {
        TODO("Not yet implemented")
    }
}
