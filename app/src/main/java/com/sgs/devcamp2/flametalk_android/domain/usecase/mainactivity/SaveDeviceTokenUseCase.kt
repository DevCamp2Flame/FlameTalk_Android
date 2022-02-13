package com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenReq
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/13
 */
class SaveDeviceTokenUseCase @Inject constructor(
    private val repository: DeviceRepository
) {
    suspend fun invoke(saveDeviceTokenReq: SaveDeviceTokenReq): Flow<Results<SaveDeviceTokenRes, WrappedResponse<SaveDeviceTokenRes>>> {
        return repository.saveDeviceToken(saveDeviceTokenReq)
    }
}
