package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.request.friend.AddContactFriendRequest
import com.sgs.devcamp2.flametalk_android.network.request.friend.AddFriendRequest
import com.sgs.devcamp2.flametalk_android.network.request.friend.FriendStatusRequest
import com.sgs.devcamp2.flametalk_android.network.response.CommonResponse
import com.sgs.devcamp2.flametalk_android.network.response.friend.AddFriendResponse
import com.sgs.devcamp2.flametalk_android.network.response.friend.FriendStatusResponse
import retrofit2.http.*

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 관련 기능 통신 인터페이스
 */
interface FriendService {
    // 연락처 친구 추가
    @POST("/api/membership/friend/contact")
    suspend fun postContactFriendAdd(
        @Body request: AddContactFriendRequest
    ): CommonResponse

    // 친구 추가
    @POST("/api/membership/friend")
    suspend fun postFriendAdd(
        @Body request: AddFriendRequest
    ): AddFriendResponse

    // 친구 리스트 조회
    @GET("/api/membership/friend")
    suspend fun getFriendList(
        @Query("isBirthday") isBirthday: Boolean?,
        @Query("isHidden") isHidden: Boolean?,
        @Query("isBlocked") isBlocked: Boolean?
    )

    // 친구 숨김, 차단여부 변경
    @PUT("/api/membership/friend/{friendId}")
    suspend fun putFriendStatus(
        @Path("friendId") friendId: Long,
        @Body request: FriendStatusRequest
    ): FriendStatusResponse
}
