package com.sgs.devcamp2.flametalk_android.domain.usecase.myopenchatprofile

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.GetOpenProfileListRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.OpenProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/06
 */
class GetOpenProfileListUseCase @Inject constructor(
    private val repository: OpenProfileRepository
) {
    suspend fun invoke(): Flow<Results<GetOpenProfileListRes, WrappedResponse<GetOpenProfileListRes>>> {
        return repository.getOpenProfileList()
    }
}
