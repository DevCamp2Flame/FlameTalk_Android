package com.sgs.devcamp2.flametalk_android.ui.friend.blocked

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend
import com.sgs.devcamp2.flametalk_android.data.model.friend.FriendStatus
import com.sgs.devcamp2.flametalk_android.domain.repository.FriendRepository
import com.sgs.devcamp2.flametalk_android.network.request.friend.FriendStatusRequest
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/02/13
 * @updated 2022/02/16
 * @desc 차단 친구 리스트. 차단 여부 변경이 가능하다.
 *       친구들 별 차단 여부 변경을 위해 HashMap을 이용하여 <친구아이디, 차단여부>를 저장하는 방식으로 차단 여부에 정확한 값을 보냄
 */

@HiltViewModel
class BlockedViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val friendRepository: Lazy<FriendRepository>
) : ViewModel() {

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

    // 유저에게 피드백 해야하는 에러 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 디버그용 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    // 차단 친구의 Key-Value <assignedFriendId-isBlocked>
    private val blockMap = mutableMapOf<Long, Boolean>()

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

                    // HashMap에 friendId와 isBlock 값을 매핑한다
                    for (i in 0 until response.data.size) {
                        blockMap.put(_blockedFriend.value!![i].friendId, true)
                    }
                } else {
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                Timber.d("Fail Response: $error")
            }
        }
    }

    fun changeBlockStatue(friendId: Long, assignedProfileId: Long) {
        viewModelScope.launch {
            try {
                val request = FriendStatusRequest(
                    assignedProfileId = assignedProfileId,
                    isMarked = true,
                    isHidden = true,
                    isBlocked = !(blockMap[friendId]!!),
                )

                val response = friendRepository.get().putFriendStatus(friendId, request)

                if (response.status == 200) {
                    _selectedFriend.value = response.data
                    blockMap[friendId] = !(blockMap[friendId]!!)
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
