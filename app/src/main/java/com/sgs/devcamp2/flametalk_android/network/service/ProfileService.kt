package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileUpdateRequest
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
}
