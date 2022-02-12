package com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.OpenProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/06
 */
class UpdateOpenProfileUseCase @Inject constructor(
    val repository: OpenProfileRepository
) {
    suspend fun invoke(
        openProfileId: Long,
        updateOpenProfileReq: UpdateOpenProfileReq
    ): Flow<Results<UpdateOpenProfileRes, WrappedResponse<UpdateOpenProfileRes>>> {
        return repository.updateOpenProfile(openProfileId, updateOpenProfileReq)
    }
}
