package com.sgs.devcamp2.flametalk_android.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyUser
import com.sgs.devcamp2.flametalk_android.network.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.data.model.ProfilePreview
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
    private var dummyUserData: ProfilePreview = getDummyUser()

    // 메인 유저 정보 -> 동작하지 않음
    private val _userProfile = MutableStateFlow(dummyUserData)
    val userProfile = _userProfile.asStateFlow()

    // 닉네임
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    // 프로필 상태메세지
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    // 프로필 이미지
    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()

    // 배경 이미지
    private val _backgroundImage = MutableStateFlow("")
    val backgroundImage = _backgroundImage.asStateFlow()

    // 에러 // TODO: ERROR response를 따로 받아야 한다.
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        // _userProfile.value = dummyUserData
    }

    fun getProfileData(profileId: Long) {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().getProfile(profileId)
                _nickname.value = "닉네임"
                _description.value = response.data.description.toString()
                _profileImage.value = response.data.imageUrl.toString()
                _backgroundImage.value = response.data.bgImageUrl.toString()
                Timber.d(response.toString())
            } catch (ignored: Throwable) {
                // TODO: 프로필 조회 실패 error 처리
                Timber.d("알 수 없는 오류 발생")
            }
        }
    }
}
