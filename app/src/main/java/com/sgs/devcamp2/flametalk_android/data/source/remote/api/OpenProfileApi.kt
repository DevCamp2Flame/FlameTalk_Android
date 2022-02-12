package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofile.GetOpenProfileRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.GetOpenProfileListRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileRes
import retrofit2.Response
import retrofit2.http.*

/**
 * @author boris
 * @created 2022/02/05
 */
interface OpenProfileApi {
    @POST("/api/membership/open-profile")
    suspend fun createOpenProfile(@Body createOpenProfileReq: CreateOpenProfileReq): Response<WrappedResponse<CreateOpenProfileRes>>

    @GET("/api/membership/open-profile/{openProfileId}")
    suspend fun getOpenProfile(@Path("openProfileId") openProfileId: Long): Response<WrappedResponse<GetOpenProfileRes>>

    @GET("/api/membership/open-profile")
    suspend fun getOpenProfileList(): Response<WrappedResponse<GetOpenProfileListRes>>

    @PUT("/api/membership/open-profile/{openProfileId}")
    suspend fun updateOpenProfile(@Path("openProfileId") openProfileId: Long, @Body updateOpenProfileReq: UpdateOpenProfileReq): Response<WrappedResponse<UpdateOpenProfileRes>>

    @DELETE("/api/membership/open-profile/{openProfileId}")
    suspend fun deleteOpenProfile(@Path("openProfileId") openProfileId: Long): Response<WrappedResponse<Nothing>>
}
