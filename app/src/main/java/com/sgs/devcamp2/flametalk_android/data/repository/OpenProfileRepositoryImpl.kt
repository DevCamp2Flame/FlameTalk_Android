package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofile.GetOpenProfileRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.GetOpenProfileListRes
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileRes
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.OpenProfileApi
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.OpenProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/05
 */
class OpenProfileRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val local: AppDatabase,
    private val remote: OpenProfileApi
) : OpenProfileRepository {
    override suspend fun createOpenProfile(createOpenProfileReq: CreateOpenProfileReq): Flow<Results<CreateOpenProfileRes, WrappedResponse<CreateOpenProfileRes>>> {
        return flow {
            val response = remote.createOpenProfile(createOpenProfileReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }
    override suspend fun getOpenProfile(openProfileId: Long): Flow<Results<GetOpenProfileRes, WrappedResponse<GetOpenProfileRes>>> {
        return flow {
            val response = remote.getOpenProfile(openProfileId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }
    override suspend fun getOpenProfileList(): Flow<Results<GetOpenProfileListRes, WrappedResponse<GetOpenProfileListRes>>> {
        return flow {
            val response = remote.getOpenProfileList()
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }
    override suspend fun updateOpenProfile(openProfileId: Long, updateOpenProfileReq: UpdateOpenProfileReq): Flow<Results<UpdateOpenProfileRes, WrappedResponse<UpdateOpenProfileRes>>> {
        return flow {
            val response = remote.updateOpenProfile(openProfileId, updateOpenProfileReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                }
            }
        }.flowOn(ioDispatcher)
    }
    override suspend fun deleteOpenProfile(openProfileId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.deleteOpenProfile(openProfileId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    emit(Results.Success(true))
                }
            }
        }.flowOn(ioDispatcher)
    }
}
