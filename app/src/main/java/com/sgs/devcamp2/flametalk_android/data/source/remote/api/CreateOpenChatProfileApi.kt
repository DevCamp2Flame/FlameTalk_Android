package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.createopenchatprofile.CreateOpenChatProfileRes
import retrofit2.Response
import retrofit2.http.GET

/**
 * @author boris
 * @created 2022/02/03
 */
interface CreateOpenChatProfileApi {

    @GET("/api/membership/profile")
    suspend fun getUserProfile(): Response<WrappedResponse<CreateOpenChatProfileRes>>
}
