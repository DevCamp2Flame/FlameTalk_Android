package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.request.sign.SigninRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.SignupRequest
import com.sgs.devcamp2.flametalk_android.network.response.sign.SigninResponse
import com.sgs.devcamp2.flametalk_android.network.response.sign.SignupResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/api/auth/signin")
    suspend fun postSignin(
        @Body request: SigninRequest
    ): SigninResponse

    @POST("/api/auth/signup")
    suspend fun postSignup(
        @Body request: SignupRequest
    ): SignupResponse
}
