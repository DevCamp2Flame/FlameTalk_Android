package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenReq
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/02/13
 */
interface DeviceRepository {
    suspend fun saveDeviceToken(saveDeviceTokenReq: SaveDeviceTokenReq): Flow<Results<SaveDeviceTokenRes, WrappedResponse<SaveDeviceTokenRes>>>
}
