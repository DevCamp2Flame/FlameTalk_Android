package com.sgs.devcamp2.flametalk_android.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyUser
import com.sgs.devcamp2.flametalk_android.data.model.Profile
import com.sgs.devcamp2.flametalk_android.data.model.ProfileDummyPreview
import com.sgs.devcamp2.flametalk_android.network.repository.ProfileRepository
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
    private val profileRepository: Lazy<ProfileRepository>
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyUserData: ProfileDummyPreview = getDummyUser()

    // 메인 유저 정보
    private val _userProfile: MutableStateFlow<Profile?> = MutableStateFlow(null)
    val userProfile = _userProfile?.asStateFlow()

    // 프로필 아이디
    private val _profileId = MutableStateFlow<Long>(0)
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

    // 사용자에게 피드백 할 에러메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    fun getProfileData(profileId: Long) {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().getProfile(profileId)

                if (response.status == 200) {
                    _userProfile.value = response.data
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
}
