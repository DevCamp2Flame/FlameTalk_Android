package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author boris
 * @created 2022/01/27
 * retrofit에서 사용될 api interface
 * local에서 chatroomList를 가져올 예정이라 현재 미사용
 */
interface ChatRoomsApi {

    @GET("user_chatroom/{user_id}")
    fun getChatRoomList(@Path("user_id") user_id: String): List<ChatRoom>
}
