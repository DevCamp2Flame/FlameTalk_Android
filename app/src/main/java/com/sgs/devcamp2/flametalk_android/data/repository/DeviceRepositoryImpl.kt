package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.common.Status
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
     * 로그인 성공시 fcm 의 devicetoken 을 업데이트하는 function 입니다.
     */
    override suspend fun saveDeviceToken(saveDeviceTokenReq: SaveDeviceTokenReq): Flow<Results<SaveDeviceTokenRes, WrappedResponse<SaveDeviceTokenRes>>> {
        return flow {
            val response = remote.saveDeviceToken(saveDeviceTokenReq)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val body = response.body()!!
                        val data = body.data!!
                        emit(Results.Success(data))
                    }
                    Status.BAD_REQUEST -> {
                        emit(Results.Error("잘못된 요청입니다"))
                    }
                    Status.UNAUTHORIZED -> {
                        emit(Results.Error("권한이 없습니다"))
                    }
                    else -> {
                        emit(Results.Error("서버 에러입니다"))
                    }
                }
            }
        }.flowOn(ioDispatcher)
    }
}
