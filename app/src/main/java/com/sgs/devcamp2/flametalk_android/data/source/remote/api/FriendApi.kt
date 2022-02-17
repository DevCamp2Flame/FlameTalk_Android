package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatrom.FriendListRes
import retrofit2.Response
import retrofit2.http.*

/**
 * @author boris
 * @created 2022/02/17
 */
interface FriendApi {

    // 친구 리스트 조회
    @GET("/api/membership/friend")
    suspend fun getFriendList(
        @Query("isBirthday") isBirthday: Boolean?,
        @Query("isHidden") isHidden: Boolean?,
        @Query("isBlocked") isBlocked: Boolean?
    ): Response<WrappedResponse<List<FriendListRes>>>
}
