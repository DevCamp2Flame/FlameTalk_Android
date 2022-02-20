package com.sgs.devcamp2.flametalk_android.data.repository

import android.util.Log
import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenReq
import com.sgs.devcamp2.flametalk_android.data.model.device.saveDeviceToken.SaveDeviceTokenRes
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.DeviceApi
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.DeviceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/13
 */
class DeviceRepositoryImpl @Inject constructor(
    val ioDispatcher: CoroutineDispatcher,
    val remote: DeviceApi
) : DeviceRepository {
    /**
     * 로그인 성공시 fcm의 devicetoken을 업데이트하는 function입니다.
     */
    val TAG: String = "로그"
    override suspend fun saveDeviceToken(saveDeviceTokenReq: SaveDeviceTokenReq): Flow<Results<SaveDeviceTokenRes, WrappedResponse<SaveDeviceTokenRes>>> {
        return flow {
            val response = remote.saveDeviceToken(saveDeviceTokenReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else {
                    Log.d(TAG, "status - ${response.body()!!.status}")
                }
            } else {
                Log.d(TAG, "DeviceRepositoryImpl -${response.code()}")
                Log.d(TAG, "DeviceRepositoryImpl -${response.errorBody()}")
            }
        }.flowOn(ioDispatcher)
    }
}
