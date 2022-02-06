package com.sgs.devcamp2.flametalk_android.network.service

import com.sgs.devcamp2.flametalk_android.network.response.sign.*
import retrofit2.http.*

interface AuthService {
    // 토큰 재발급
    @GET("/api/auth/token")
    suspend fun getRenewToken(): RenewTokenResponse
}
