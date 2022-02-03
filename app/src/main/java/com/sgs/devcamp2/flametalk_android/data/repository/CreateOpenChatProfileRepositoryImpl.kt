package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToProfileEntity
import com.sgs.devcamp2.flametalk_android.data.model.createopenchatprofile.CreateOpenChatProfileRes
import com.sgs.devcamp2.flametalk_android.data.model.createopenchatprofile.Profiles
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.CreateOpenChatProfileApi
import com.sgs.devcamp2.flametalk_android.domain.entity.ProfileEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.CreateOpenChatProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/03
 */
class CreateOpenChatProfileRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val createOpenChatProfileApi: CreateOpenChatProfileApi
) : CreateOpenChatProfileRepository {
    override suspend fun getUserProfile(): Flow<Results<ProfileEntity, WrappedResponse<CreateOpenChatProfileRes>>> {
        return flow {
            val response = createOpenChatProfileApi.getUserProfile()
            if (response.isSuccessful) {
                val profile: List<Profiles> = response.body()?.data?.profiles!!
                var defaultProfile: ProfileEntity
                for (i in 0..profile.size) {
                    if (profile[i].isDefault) {
                        defaultProfile = mapperToProfileEntity(profile[i])
                        emit(Results.Success(defaultProfile))
                    }
                }
            }
        }.flowOn(ioDispatcher)
    }
}
