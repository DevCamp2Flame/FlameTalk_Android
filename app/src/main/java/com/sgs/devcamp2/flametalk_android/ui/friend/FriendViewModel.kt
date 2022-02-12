package com.sgs.devcamp2.flametalk_android.ui.friend

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getBirthdayFriend
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyFriend
import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend
import com.sgs.devcamp2.flametalk_android.data.model.profile.ProfilePreview
import com.sgs.devcamp2.flametalk_android.network.repository.FriendRepository
import com.sgs.devcamp2.flametalk_android.network.repository.ProfileRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FriendViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileRepository: Lazy<ProfileRepository>,
    private val friendRepository: Lazy<FriendRepository>
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyBirthdayData: List<Friend> = getBirthdayFriend()
    private var dummyFriendData: List<Friend> = getDummyFriend()

    // 유저 닉네임
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    // 유저 아이디
    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    // 메인 유저 정보
    private val _userProfile: MutableStateFlow<ProfilePreview?> = MutableStateFlow(null)
    val userProfile = _userProfile?.asStateFlow()

    // 유저 멀티프로필
    private val _multiProfile = MutableStateFlow<List<ProfilePreview>>(emptyList())
    val multiProfile: MutableStateFlow<List<ProfilePreview>> = _multiProfile

    // 생일인 친구 리스트
    private val _birthProfile = MutableStateFlow<List<Friend>?>(emptyList())
    val birthProfile: MutableStateFlow<List<Friend>?> = _birthProfile

    // 친구 리스트
    private val _friendProfile = MutableStateFlow<List<Friend>>(emptyList())
    val friendProfile: MutableStateFlow<List<Friend>> = _friendProfile

    // 유저에게 피드백 해야하는 에러 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 디버그용 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        getProfileList()
        // getFriendList(BIRTHDAY_FRIEND)
        // getFriendList(FRIEND)

        _birthProfile.value = dummyBirthdayData
        _friendProfile.value = dummyFriendData
    }

    fun getProfileList() {
        viewModelScope.launch {
            try {
                val response = profileRepository.get().getProfileList()
                val multiProfile = ArrayList<ProfilePreview>()

                if (response.status == 200) {
                    _nickname.value = response.data.nickname
                    _userId.value = response.data.userId
                    response.data.profiles.map {
                        when (it.isDefault) {
                            // 기본 프로필
                            true -> _userProfile?.value = it
                            // 추가로 생성한 멀티 프로필
                            false -> multiProfile.add(it)
                        }
                    }
                    _multiProfile.value = multiProfile.toList()

                    // Result
                    Timber.d("User Profile ${_userProfile?.value}")
                    Timber.d("Multi Profile ${_multiProfile.value}")
                } else {
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                _error.value = error.toString()
                Timber.d("Fail Response: $_error")
            }
        }
    }

    fun getFriendList(type: Int) {
        viewModelScope.launch {
            try {
                val response = when (type) {
                    BIRTHDAY_FRIEND -> friendRepository.get().getFriendList(true, false, false)
                    FRIEND -> friendRepository.get().getFriendList(null, false, false)
                    else -> null
                }

                if (response?.status == 200) {
                    _birthProfile.value = response.data

                    // Result
                    Timber.d("Birthday Response ${response.data}")
                    Timber.d("Birthday Friend ${_birthProfile.value}")
                } else {
                    _message.value = response!!.message
                }
            } catch (error: Throwable) {
                _error.value = error.toString()
                Timber.d("Fail Response: $_error")
            }
        }
    }

    companion object {
        const val BIRTHDAY_FRIEND = 100
        const val FRIEND = 200
    }
}
