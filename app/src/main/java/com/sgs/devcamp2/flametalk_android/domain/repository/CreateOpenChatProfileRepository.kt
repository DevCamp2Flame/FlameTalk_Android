package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.createopenchatprofile.CreateOpenChatProfileRes
import com.sgs.devcamp2.flametalk_android.domain.entity.ProfileEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/02/03
 */
interface CreateOpenChatProfileRepository {
    suspend fun getUserProfile(): Flow<Results<ProfileEntity, WrappedResponse<CreateOpenChatProfileRes>>>
}
