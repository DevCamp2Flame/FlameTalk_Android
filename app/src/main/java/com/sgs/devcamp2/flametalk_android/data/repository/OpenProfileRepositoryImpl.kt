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
 * @author 김현국
 * @created 2022/02/05
 */
class OpenProfileRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val local: AppDatabase,
    private val remote: OpenProfileApi
) : OpenProfileRepository {
    /**
     * 오픈프로필을 생성하는 function입니다
     * @param createOpenProfileReq 오픈프로필 생성 request model
     */
    override suspend fun createOpenProfile(createOpenProfileReq: CreateOpenProfileReq): Flow<Results<CreateOpenProfileRes, WrappedResponse<CreateOpenProfileRes>>> {
        return flow {
            val response = remote.createOpenProfile(createOpenProfileReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("잘못된 요청입니다"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("권한이 없습니다"))
                } else {
                    emit(Results.Error("서버 에러입니다"))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 오픈 프로필 상세보기 function입니다.
     */
    override suspend fun getOpenProfile(openProfileId: Long): Flow<Results<GetOpenProfileRes, WrappedResponse<GetOpenProfileRes>>> {
        return flow {
            val response = remote.getOpenProfile(openProfileId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("잘못된 요청입니다"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("권한이 없습니다"))
                } else {
                    emit(Results.Error("서버 에러입니다"))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 오픈 프로필 리스트 조회 function입니다.
     */
    override suspend fun getOpenProfileList(): Flow<Results<GetOpenProfileListRes, WrappedResponse<GetOpenProfileListRes>>> {
        return flow {
            val response = remote.getOpenProfileList()
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("잘못된 요청입니다"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("권한이 없습니다"))
                } else {
                    emit(Results.Error("서버 에러입니다"))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 오픈 프로필 업데이트 function입니다.
     */
    override suspend fun updateOpenProfile(openProfileId: Long, updateOpenProfileReq: UpdateOpenProfileReq): Flow<Results<UpdateOpenProfileRes, WrappedResponse<UpdateOpenProfileRes>>> {
        return flow {
            val response = remote.updateOpenProfile(openProfileId, updateOpenProfileReq)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    val body = response.body()!!
                    val data = body.data!!
                    emit(Results.Success(data))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("잘못된 요청입니다"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("권한이 없습니다"))
                } else {
                    emit(Results.Error("서버 에러입니다"))
                }
            }
        }.flowOn(ioDispatcher)
    }
    /**
     * 오픈 프로필 삭제 function입니다.
     */
    override suspend fun deleteOpenProfile(openProfileId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return flow {
            val response = remote.deleteOpenProfile(openProfileId)
            if (response.isSuccessful) {
                if (response.body()!!.status == 200) {
                    emit(Results.Success(true))
                } else if (response.body()!!.status == 400) {
                    emit(Results.Error("잘못된 요청입니다"))
                } else if (response.body()!!.status == 401) {
                    emit(Results.Error("권한이 없습니다"))
                } else {
                    emit(Results.Error("서버 에러입니다"))
                }
            }
        }.flowOn(ioDispatcher)
    }
}
