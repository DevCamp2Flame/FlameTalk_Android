package com.sgs.devcamp2.flametalk_android.ui.friend.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyProfiles
import com.sgs.devcamp2.flametalk_android.data.model.profile.ProfilePreview
import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
import com.sgs.devcamp2.flametalk_android.network.repository.FriendRepository
import com.sgs.devcamp2.flametalk_android.network.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.request.friend.AddFriendRequest
import com.sgs.devcamp2.flametalk_android.network.response.friend.AddFriendResponse
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences,
    private val friendRepository: Lazy<FriendRepository>,
    private val profileRepository: Lazy<ProfileRepository>
) : ViewModel() {
    private var dummyProfileData: List<ProfilePreview> = getDummyProfiles()

    // 유저 프로필 리스트
    private val _profiles = MutableStateFlow<List<ProfilePreview>>(emptyList())
    val profiles: MutableStateFlow<List<ProfilePreview>> = _profiles

    // 선택된 프로필
    private val _profileId = MutableStateFlow(0L)
    val profileId = _profileId.asStateFlow()

    // 닉네임
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    // 추가된 친구 데이터
    private val _friendData: MutableStateFlow<AddFriendResponse.Data?> = MutableStateFlow(null)
    val friendData = _friendData.asStateFlow()

    // 친구 추가 성공 여부
    private val _isSuccess: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isSuccess = _isSuccess.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    init {
        // 유저 프로필 리스트 불러오기
        getProfileList()

        _nickname.value = "소연"
    }

    fun clickedProfile(profileId: Long) {
        _profileId.value = profileId
    }

    fun addFriend(phoneNumber: String) {
        viewModelScope.launch {
            try {
                val request = AddFriendRequest(_profileId.value, phoneNumber)
                val response = friendRepository.get().postFriendAdd(request)

                if (response.status == 200) {
                    _friendData.value = response.data
                    _isSuccess.value = true
                } else {
                    _isSuccess.value = false
                    _message.value = response.message
                }
                _message.value = null
                _isSuccess.value = null
            } catch (ignored: Throwable) {
                Timber.d("Fail Response: $ignored")
            }
        }
    }

    private fun getProfileList() {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().getProfileList()

                if (response.status == 200) {
                    _profiles.value = response.data.profiles

                    // Result
                    Timber.d("User Profile ${_profiles?.value}")
                } else {
                    _message.value = response.message
                }
            } catch (ignored: Throwable) {
                Timber.d("Fail Response: $ignored")
            }
        }
    }
}
