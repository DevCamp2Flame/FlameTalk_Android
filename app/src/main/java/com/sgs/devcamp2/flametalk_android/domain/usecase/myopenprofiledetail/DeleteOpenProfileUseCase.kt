package com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.OpenProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/06
 */
class DeleteOpenProfileUseCase @Inject constructor(
    val repository: OpenProfileRepository
) {
    suspend fun invoke(openProfileId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return repository.deleteOpenProfile(openProfileId)
    }
}
