package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author boris
 * @created 2022/01/28
 * 채팅방 생성시 post api
 */
interface InviteRoomApi {

    @POST("/api/chat/room")
    suspend fun createChatRoom(@Body inviteRoomReq: InviteRoomReq): Response<WrappedResponse<InviteRoomRes>>
}
