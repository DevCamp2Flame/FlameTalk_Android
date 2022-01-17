package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.request.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/sign-up")
    suspend fun postSignUp(@Body request: SignUpRequest)
}