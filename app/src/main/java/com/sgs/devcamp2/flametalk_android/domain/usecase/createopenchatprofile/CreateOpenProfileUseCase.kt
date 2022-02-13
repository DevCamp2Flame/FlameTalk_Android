package com.sgs.devcamp2.flametalk_android.domain.usecase.createopenchatprofile

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.OpenProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/03
 */
class CreateOpenProfileUseCase @Inject constructor(
    private val repository: OpenProfileRepository
) {
    suspend fun invoke(createOpenProfileReq: CreateOpenProfileReq): Flow<Results<CreateOpenProfileRes, WrappedResponse<CreateOpenProfileRes>>> {
        return repository.createOpenProfile(createOpenProfileReq)
    }
}
