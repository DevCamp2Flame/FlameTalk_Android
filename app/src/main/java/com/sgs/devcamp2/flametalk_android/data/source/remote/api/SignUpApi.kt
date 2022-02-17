package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthReq
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author boris
 * @created 2022/02/18
 */
interface SignUpApi {
    @POST("/api/auth/signup")
    suspend fun signUp(@Body authReq: AuthReq): Response<WrappedResponse<AuthRes>>
}
