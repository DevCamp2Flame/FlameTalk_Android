package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthReq
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/02/18
 */
interface AuthRepository {
    fun signUp(authReq: AuthReq): Flow<Results<AuthRes, WrappedResponse<AuthRes>>>
}
