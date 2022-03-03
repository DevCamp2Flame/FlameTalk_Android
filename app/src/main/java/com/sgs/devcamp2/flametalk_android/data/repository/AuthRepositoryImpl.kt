package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.common.Status
import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthReq
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthRes
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.SignUpApi
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/18
 */
class AuthRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val remote: SignUpApi
) : AuthRepository {
    override fun signUp(authReq: AuthReq): Flow<Results<AuthRes, WrappedResponse<AuthRes>>> {
        return flow {
            val response = remote.signUp(authReq)
            if (response.isSuccessful) {
                when (response.body()!!.status) {
                    Status.OK -> {
                        val data = response.body()!!.data
                        emit(Results.Success(data!!))
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
