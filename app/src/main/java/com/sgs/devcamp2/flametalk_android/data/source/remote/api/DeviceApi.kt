package com.sgs.devcamp2.flametalk_android.data.source.remote.api

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenReq
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author boris
 * @created 2022/02/13
 */
interface DeviceApi {
    // 토큰 저장
    @POST("/api/device/token")
    suspend fun saveDeviceToken(@Body saveDeviceTokenReq: SaveDeviceTokenReq): Response<WrappedResponse<SaveDeviceTokenRes>>
}
