package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileService {
    // 프로필 생성
    @POST("/api/membership/profile/me")
    suspend fun postProfileCreate(@Body request: ProfileCreateRequest): ResponseBody
}
