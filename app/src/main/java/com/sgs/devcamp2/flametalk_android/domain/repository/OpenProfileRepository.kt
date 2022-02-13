package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofile.GetOpenProfileRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.GetOpenProfileListRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import kotlinx.coroutines.flow.Flow

/**
 * @author 김현국
 * @created 2022/02/05
 */
interface OpenProfileRepository {
    suspend fun createOpenProfile(createOpenProfileReq: CreateOpenProfileReq): Flow<Results<CreateOpenProfileRes, WrappedResponse<CreateOpenProfileRes>>>
    suspend fun getOpenProfile(openProfileId: Long): Flow<Results<GetOpenProfileRes, WrappedResponse<GetOpenProfileRes>>>
    suspend fun getOpenProfileList(): Flow<Results<GetOpenProfileListRes, WrappedResponse<GetOpenProfileListRes>>>
    suspend fun updateOpenProfile(openProfileId: Long, updateOpenProfileReq: UpdateOpenProfileReq): Flow<Results<UpdateOpenProfileRes, WrappedResponse<UpdateOpenProfileRes>>>
    suspend fun deleteOpenProfile(openProfileId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>>
}
