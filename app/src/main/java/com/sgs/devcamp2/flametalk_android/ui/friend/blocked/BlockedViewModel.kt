package com.sgs.devcamp2.flametalk_android.ui.friend.blocked

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getBirthdayFriend
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyFriend
import com.sgs.devcamp2.flametalk_android.data.model.Friend
import com.sgs.devcamp2.flametalk_android.data.model.FriendStatus
import com.sgs.devcamp2.flametalk_android.network.repository.FriendRepository
import com.sgs.devcamp2.flametalk_android.network.request.friend.FriendStatusRequest
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class BlockedViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
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

    // 차단된 친구 리스트
    private val _blockedFriend = MutableStateFlow<List<Friend>?>(emptyList())
    val blockedFriend: MutableStateFlow<List<Friend>?> = _blockedFriend

    // 선택된 친구
    private val _selectedFriend: MutableStateFlow<FriendStatus?> = MutableStateFlow(null)
    val selectedFriend: MutableStateFlow<FriendStatus?> = _selectedFriend

    // 설정 값
    private val _isSelected = MutableStateFlow(true)
    val isSelected = _isSelected.asStateFlow()

    // 유저에게 피드백 해야하는 에러 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 디버그용 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        getBlockedFriendList()
    }

    // 차단된 친구 리스트 요청
    private fun getBlockedFriendList() {
        viewModelScope.launch {
            try {
                val response = friendRepository.get().getFriendList(null, null, true)

                if (response.status == 200) {
                    _blockedFriend.value = response.data

                    // Result
                    Timber.d("Blocked Response ${response.data}")
                    Timber.d("Blocked Friend ${_blockedFriend.value}")
                } else {
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                Timber.d("Fail Response: $error")
            }
        }
    }

    fun changeBlockStatue(friendId: Long) {
        viewModelScope.launch {
            try {
                val request = FriendStatusRequest(
                    // TODO: 친구 리스트 조회 시, isMarked, isHidden, isBlocked 속성 값을 같이 보내줘야 함
                    assignedProfileId = _selectedFriend.value!!.preview.profileId,
                    isMarked = true,
                    isHidden = true,
                    isBlocked = !_isSelected.value, // 사실 이것도 이상함
                )
                val response = friendRepository.get().putFriendStatus(friendId, request)

                if (response.status == 200) {
                    _selectedFriend.value = response.data
                    _isSelected.value = !_isSelected.value

                    // Result
                    Timber.d("Blocked Response ${response.data}")
                    Timber.d("Blocked Friend ${_blockedFriend.value}")
                } else {
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                Timber.d("Fail Response: $error")
            }
        }
    }
}
