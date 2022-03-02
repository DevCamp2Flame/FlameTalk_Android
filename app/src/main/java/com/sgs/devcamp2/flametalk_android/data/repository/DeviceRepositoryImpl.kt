package com.sgs.devcamp2.flametalk_android.data.repository

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
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("잘못된 요청입니다"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("권한이 없습니다"))
                } else {
                    emit(Results.Error("서버 에러입니다"))
                }
            }
        }.flowOn(ioDispatcher)
    }
}
