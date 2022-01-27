package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.request.sign.SigninRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.SignupRequest
import com.sgs.devcamp2.flametalk_android.network.response.sign.*
import retrofit2.http.*

interface UserService {
    // 회원가입
    @POST("/api/auth/signup")
    suspend fun postSignup(
        @Body request: SignupRequest
    ): SignupResponse

    // 로그인
    @POST("/api/auth/signin")
    suspend fun postSignin(
        @Body request: SigninRequest
    ): SigninResponse

    // 이메일 중복체크
    @GET("/api/auth/check")
    suspend fun getEmailCheck(
        @Query("email") email: String
    ): EmailCheckResponse

    // 토큰 재발급
//    @GET("/api/auth/token")
//    suspend fun getRenewToken(): RenewTokenResponse

    // 탈퇴
    @DELETE("/api/auth/leave")
    suspend fun deleteLeaveUser(): UserLeaveResponse
}
