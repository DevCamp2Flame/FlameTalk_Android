package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileUpdateRequest
import com.sgs.devcamp2.flametalk_android.network.response.CommonResponse
import com.sgs.devcamp2.flametalk_android.network.response.feed.ProfileFeedResponse
import com.sgs.devcamp2.flametalk_android.network.response.profile.ProfileListResponse
import com.sgs.devcamp2.flametalk_android.network.response.profile.ProfileResponse
import retrofit2.http.*

/**
 * @author 박소연
 * @created 2022/01/25
 * @desc 프로필, 프로필 피드와 관련된 네트워크 통신 인터페이스
 */

interface ProfileService {
    /*  프로필 */
    // 프로필 생성
    @POST("/api/membership/profile")
    suspend fun postProfileCreate(
        @Body request: ProfileCreateRequest
    ): CommonResponse

    // 프로필 수정
    @PUT("/api/membership/profile/{profileId}")
    suspend fun putProfileUpdate(
        @Path("profileId") profileId: Long,
        @Body request: ProfileUpdateRequest
    ): CommonResponse

    // 프로필 조회
    @GET("/api/membership/profile/{profileId}")
    suspend fun getProfile(
        @Path("profileId") profileId: Long
    ): ProfileResponse

    // 프로필 리스트 조회 (기본프로필, 멀티프로필)
    @GET("/api/membership/profile")
    suspend fun getProfileList(): ProfileListResponse

    // 프로필 삭제
    @DELETE("/api/membership/profile/{profileId}")
    suspend fun deleteProfile(
        @Path("profileId") profileId: Long
    ): CommonResponse

    /*  피드 */
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
