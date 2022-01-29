package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author boris
 * @created 2022/01/27
 */
interface ChatRoomsApi {

    @GET("user_chatroom/{user_id}")
    fun getChatRoomList(@Path("user_id") user_id: String): Flow<List<ChatRoom>>
}
