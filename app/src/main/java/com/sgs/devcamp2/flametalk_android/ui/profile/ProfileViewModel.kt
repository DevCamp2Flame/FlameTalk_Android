package com.sgs.devcamp2.flametalk_android.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.Sticker
import com.sgs.devcamp2.flametalk_android.data.model.profile.Profile
import com.sgs.devcamp2.flametalk_android.domain.repository.FriendRepository
import com.sgs.devcamp2.flametalk_android.domain.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.request.friend.FriendStatusRequest
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepository: Lazy<ProfileRepository>,
    private val friendRepository: Lazy<FriendRepository>
) : ViewModel() {

    // 프로필 유저 정보
    private val _userProfile: MutableStateFlow<Profile?> = MutableStateFlow(null)
    val userProfile = _userProfile.asStateFlow()

    // 프로필 아이디
    private val _profileId = MutableStateFlow<Long>(0L)
    val profileId = _profileId.asStateFlow()

    // 프로필 상태메세지
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    // 프로필 이미지
    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()

    // 배경 이미지
    private val _backgroundImage = MutableStateFlow("")
    val backgroundImage = _backgroundImage.asStateFlow()

    // 스티커
    private val _stickers = MutableStateFlow<List<Sticker>>(emptyList())
    val stickers: MutableStateFlow<List<Sticker>> = _stickers

    // 성공 여부
    private val _isSuccess: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isSuccess = _isSuccess.asStateFlow()

    // 성공 여부
    private val _deleteSuccess: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val deleteSuccess = _deleteSuccess.asStateFlow()

    // 사용자에게 피드백 할 에러메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    // 유저 한명의 프로필 데이터를 받는다
    fun getProfileData(profileId: Long) {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().getProfile(profileId)

                if (response.status == 200) {
                    _userProfile.value = response.data
                    _stickers.value = response.data.sticker
                } else {
                    _message.value = response.message
                }
                Timber.d(response.toString())
            } catch (error: Throwable) {
                _error.value = error.toString()
                Timber.d("Fail Response: $_error")
            }
        }
    }

    // 프로필 삭제
    fun deleteProfile(profileId: Long) {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().deleteProfile(profileId)

                if (response.status == 200) {
                    _isSuccess.value = true
                } else {
                    _message.value = response.message
                }
                Timber.d("Success Response: $response")
            } catch (error: Throwable) {
                Timber.d("Fail Response: $error")
            }
        }
    }

    fun changeFriendStatue(type: Int, friendId: Long, assignedProfileId: Long) {
        viewModelScope.launch {
            try {
                var mark = false
                var hide = false
                var block = false

                when (type) {
                    TO_HIDE -> hide = true
                    TO_BLOCK -> block = true
                }
                val request = FriendStatusRequest(
                    assignedProfileId = assignedProfileId,
                    isMarked = mark,
                    isHidden = hide,
                    isBlocked = block,
                )
                val response = friendRepository.get().putFriendStatus(friendId, request)

                if (response.status == 200) {
                    when (response.data.type) {
                        "DEFAULT" ->
                            _message.value = "${response.data.nickname}님의 상태를 해제했습니다."
                        "MARKED" ->
                            _message.value = "${response.data.nickname}님을 즐겨찾기로 등록했습니다."
                        "HIDDEN" ->
                            _message.value = "${response.data.nickname}님의 숨김친구로 설정했습니다."
                        "BLOCKED" ->
                            _message.value = "${response.data.nickname}님의 차단친구로 설정했습니다."
                    }
                    _isSuccess.value = true
                    Timber.d("Profile Response ${response.data}")
                    Timber.d("Type ${response.data.type}")
                } else {
                    _isSuccess.value = false
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                Timber.d("Fail Response: $error")
            }
        }
    }

    companion object {
        const val TO_BLOCK = 400
        const val TO_HIDE = 500
    }
}
