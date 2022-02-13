package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.closechatroom.CloseChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.enterchatroom.EnterChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroom.GetChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomfiles.GetChatRoomFilesRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatrom.JoinChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatrom.JoinChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom.UpdateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.updateopenchatroomprofile.UpdateOpenChatRoomProfileReq
import retrofit2.Response
import retrofit2.http.*

/**
 * @author 김현국
 * @created 2022/02/05
 */
interface ChatRoomApi {
    // 채팅방 생성
    @POST("/api/chatroom")
    suspend fun createChatRoom(@Body createChatRoomReq: CreateChatRoomReq): Response<WrappedResponse<CreateChatRoomRes>>

    // 채팅방 입장
    @POST("/api/chatroom/user")
    suspend fun enterChatRoom(@Body enterChatRoomReq: EnterChatRoomReq): Response<WrappedResponse<EnterChatRoomReq>>

    // 채팅방 조회
    @GET("/api/chatroom/{userChatroomId}")
    suspend fun getChatRoom(@Path("userChatroomId") userChatroomId: Long): Response<WrappedResponse<GetChatRoomRes>>

    // 채팅방 리스트 조회
    @GET("/api/chatroom")
    suspend fun getChatRoomList(@Query("isOpen") isOpen: Boolean): Response<WrappedResponse<GetChatRoomListRes>>

    // 채팅방 파일 리스트 조회
    @GET("/api/chatroom/file/{chatroomId}")
    suspend fun getChatRoomFileList(@Path("chatroomId") chatroomId: String): Response<WrappedResponse<List<GetChatRoomFilesRes>>>

    // 채팅방 수정
    @PUT("/api/chatroom/{userChatroomId}")
    suspend fun updateChatRoom(@Path("userChatroomId") userChatroomId: Long, @Body updateChatRoomReq: UpdateChatRoomReq): Response<WrappedResponse<UpdateChatRoomRes>>

    // 채팅방 닫기
    @PUT("/api/chatroom/close")
    suspend fun closeChatRoom(@Body closeChatRoomReq: CloseChatRoomReq): Response<WrappedResponse<Nothing>>

    // 오픈 채팅방 프로필 변경
    @PUT("/api/chatroom/profile")
    suspend fun updateOpenChatRoomProfile(@Body updateOpenChatRoomProfileReq: UpdateOpenChatRoomProfileReq): Response<WrappedResponse<Nothing>>

    // 채팅방 나가기
    @DELETE("/api/chatroom/{userChatroomId}")
    suspend fun leaveChatRoom(@Path("userChatroomId") userChatroomId: Long): Response<WrappedResponse<Nothing>>

    // 채팅방 대화상대 초대
    @POST("/api/chatroom/join")
    suspend fun joinChatRoom(@Body joinChatRoomReq: JoinChatRoomReq): Response<WrappedResponse<JoinChatRoomRes>>

}
