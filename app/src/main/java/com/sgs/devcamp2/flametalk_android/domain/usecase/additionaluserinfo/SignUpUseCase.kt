package com.sgs.devcamp2.flametalk_android.domain.usecase.additionaluserinfo

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthReq
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/18
 */
class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    fun invoke(authReq: AuthReq): Flow<Results<AuthRes, WrappedResponse<AuthRes>>> {
        return repository.signUp(authReq)
    }
}
