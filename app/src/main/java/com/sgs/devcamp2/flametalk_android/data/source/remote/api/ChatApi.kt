package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author boris
 * @created 2022/02/13
 */
interface ChatApi {

    // 채팅 메시지 조회
    @GET("/api/chat/history/{roomId}")
    suspend fun getChatMessageHistory(@Path("roomId") roomId: String, @Query("lastReadMessageId") lastReadMessage: String): Response<WrappedResponse<List<ChatRes>>>
}
