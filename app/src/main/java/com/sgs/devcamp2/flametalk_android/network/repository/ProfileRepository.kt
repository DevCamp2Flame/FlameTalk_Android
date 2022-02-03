package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileUpdateRequest
import com.sgs.devcamp2.flametalk_android.network.service.ProfileService
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * @author 박소연
 * @created 2022/01/25
 * @desc 프로필 생성, 수정, 삭제와 관련된 통신(네트워크, 로컬) 레파지토리
 */

@Singleton
class ProfileRepository @Inject constructor(
    private val profileService: Lazy<ProfileService>,
    private val userDAO: Lazy<UserDAO>,
    private val ioDispatcher: CoroutineDispatcher
) {
    // 프로필 생성
    suspend fun createProfile(request: ProfileCreateRequest) = withContext(ioDispatcher) {
        profileService.get().postProfileCreate(request)
    }

    // 프로필 수정
    suspend fun updateProfile(profileId: Long, request: ProfileUpdateRequest) =
        withContext(ioDispatcher) {
            profileService.get().putProfileUpdate(profileId, request)
        }

    // 프로필 조회
    suspend fun getProfile(profileId: Long) = withContext(ioDispatcher) {
        profileService.get().getProfile(profileId)
    }
}
