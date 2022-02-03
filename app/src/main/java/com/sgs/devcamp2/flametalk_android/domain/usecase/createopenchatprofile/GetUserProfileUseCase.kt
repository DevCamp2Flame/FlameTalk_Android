package com.sgs.devcamp2.flametalk_android.domain.usecase.createopenchatprofile

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.createopenchatprofile.CreateOpenChatProfileRes
import com.sgs.devcamp2.flametalk_android.domain.entity.ProfileEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.CreateOpenChatProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/03
 */
class GetUserProfileUseCase @Inject constructor(
    private val repository: CreateOpenChatProfileRepository
) {
    suspend fun invoke(): Flow<Results<ProfileEntity, WrappedResponse<CreateOpenChatProfileRes>>> {
        return repository.getUserProfile()
    }
}
