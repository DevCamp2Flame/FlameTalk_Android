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
 * @author 김현국
 * @created 2022/02/05
 */
interface OpenProfileApi {

    //오픈프로필 생성
    @POST("/api/membership/open-profile")
    suspend fun createOpenProfile(@Body createOpenProfileReq: CreateOpenProfileReq): Response<WrappedResponse<CreateOpenProfileRes>>

    //오픈프로필 조회
    @GET("/api/membership/open-profile/{openProfileId}")
    suspend fun getOpenProfile(@Path("openProfileId") openProfileId: Long): Response<WrappedResponse<GetOpenProfileRes>>

    //오픈프로필 리스트조회
    @GET("/api/membership/open-profile")
    suspend fun getOpenProfileList(): Response<WrappedResponse<GetOpenProfileListRes>>

    //오픈프로필 업데이트
    @PUT("/api/membership/open-profile/{openProfileId}")
    suspend fun updateOpenProfile(@Path("openProfileId") openProfileId: Long, @Body updateOpenProfileReq: UpdateOpenProfileReq): Response<WrappedResponse<UpdateOpenProfileRes>>

    //오픈프로필 삭제
    @DELETE("/api/membership/open-profile/{openProfileId}")
    suspend fun deleteOpenProfile(@Path("openProfileId") openProfileId: Long): Response<WrappedResponse<Nothing>>
}
