package com.sgs.devcamp2.flametalk_android.network.repository

import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileUpdateRequest
import com.sgs.devcamp2.flametalk_android.network.service.FileService
import com.sgs.devcamp2.flametalk_android.network.service.ProfileService
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * @author 박소연
 * @created 2022/01/25
 * @created 2022/02/06
 * @desc 프로필, 프로필 피드와 관련된 통신(네트워크, 로컬) 레파지토리
 */

@Singleton
class ProfileRepository @Inject constructor(
    private val profileService: Lazy<ProfileService>,
    private val fileService: Lazy<FileService>,
    private val userPreferences: Lazy<UserPreferences>,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     *   Profile
     */
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

    // 프로필 리스트 조회 (기본프로필, 멀티프로필)
    suspend fun getProfileList() = withContext(ioDispatcher) {
        profileService.get().getProfileList()
    }

    /**
     *   Feed
     */
// 피드 리스트 조회 (프로필 or 배경)
    suspend fun getSingleFeedList(profileId: Long, isBackground: Boolean) =
        withContext(ioDispatcher) {
            profileService.get().getSingleFeedList(profileId, isBackground)
        }

    // 피드 리스트 조회 (프로필 + 배경)
    suspend fun getTotalFeedList(profileId: Long) = withContext(ioDispatcher) {
        profileService.get().getTotalFeedList(profileId)
    }

    // 피드 삭제
    suspend fun deleteFeed(feedId: Long) = withContext(ioDispatcher) {
        profileService.get().deleteFeed(feedId)
    }

    // 피드 사진 공개 설정
    suspend fun updateFeedImageLock(feedId: Long) = withContext(ioDispatcher) {
        profileService.get().updateFeedImageLock(feedId)
    }
}
