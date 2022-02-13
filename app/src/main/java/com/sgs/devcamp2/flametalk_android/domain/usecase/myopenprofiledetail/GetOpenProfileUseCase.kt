package com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofile.GetOpenProfileRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.OpenProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/06
 */
class GetOpenProfileUseCase @Inject constructor(
    val repository: OpenProfileRepository
) {
    suspend fun invoke(openProfileId: Long): Flow<Results<GetOpenProfileRes, WrappedResponse<GetOpenProfileRes>>> {
        return repository.getOpenProfile(openProfileId)
    }
}
