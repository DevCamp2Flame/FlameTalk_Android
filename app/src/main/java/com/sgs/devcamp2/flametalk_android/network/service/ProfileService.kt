package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileUpdateRequest
import com.sgs.devcamp2.flametalk_android.network.response.CommonResponse
import com.sgs.devcamp2.flametalk_android.network.response.feed.ProfileFeedResponse
import com.sgs.devcamp2.flametalk_android.network.response.sign.ProfileResponse
import com.sgs.devcamp2.flametalk_android.network.response.sign.ProfileUpdateResponse
import retrofit2.http.*

interface ProfileService {
    // 프로필 생성
    @POST("/api/profile/me")
    suspend fun postProfileCreate(@Body request: ProfileCreateRequest)

    // 프로필 수정
    @PUT("/api/profile/me/{profileId}")
    suspend fun putProfileUpdate(
        @Path("profileId") profileId: Long,
        @Body request: ProfileUpdateRequest
    ): ProfileUpdateResponse

    // 프로필 조회
    @GET("/api/profile/{profileId}")
    suspend fun getProfile(
        @Path("profileId") profileId: Long
    ): ProfileResponse

    // 피드 리스트 조회 (프로필 or 배경)
    @GET("/api/membership/feed")
    suspend fun getSingleFeedList(
        @Query("profileId") profileId: Long,
        @Query("isBackground") isBackground: Boolean
    ): ProfileFeedResponse

    // 피드 리스트 조회 (프로필 + 배경)
    @GET("/api/membership/feed")
    suspend fun getTotalFeedList(
        @Query("profileId") profileId: Long
    ): ProfileFeedResponse

    // 피드 히스토리 삭제
    @DELETE("/api/membership/feed/{feedId}")
    suspend fun deleteFeed(
        @Path("feedId") feedId: Long
    ): CommonResponse

    // 피드 프로필 공개 여부 변경
    @PUT("/api/membership/feed/lock/{feedId}")
    suspend fun updateFeedImageLock(
        @Path("feedId") feedId: Long
    ): CommonResponse
}
