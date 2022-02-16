package com.sgs.devcamp2.flametalk_android.ui.friend.hidden

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
 * @desc 숨김 친구 리스트. 숨김 여부 변경이 가능하다.
 *       친구들 별 숨김 여부 변경을 위해 HashMap을 이용하여 <친구아이디, 숨김여부>를 저장하는 방식으로 숨김 여부에 정확한 값을 보냄
 */

@HiltViewModel
class HiddenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val friendRepository: Lazy<FriendRepository>
) : ViewModel() {

    // 유저 닉네임
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    // 유저 아이디
    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    // 숨김 친구 리스트
    private val _hiddenFriend = MutableStateFlow<List<Friend>?>(emptyList())
    val hiddenFriend: MutableStateFlow<List<Friend>?> = _hiddenFriend

    // 선택된 친구
    private val _selectedFriend: MutableStateFlow<FriendStatus?> = MutableStateFlow(null)
    val selectedFriend: MutableStateFlow<FriendStatus?> = _selectedFriend

    // 유저에게 피드백 해야하는 에러 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 디버그용 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    // 숨김 친구의 Key-Value <assignedFriendId-isHidden>
    private val hiddenMap = mutableMapOf<Long, Boolean>()

    init {
        getHiddenFriendList()
    }

    // 숨김된 친구 리스트 요청
    private fun getHiddenFriendList() {
        viewModelScope.launch {
            try {
                val response = friendRepository.get().getFriendList(null, true, null)

                if (response.status == 200 && response.data.isNotEmpty()) {
                    _hiddenFriend.value = response.data

                    // HashMap에 friendId와 isHidden 값을 매핑한다
                    for (i in 0 until response.data.size) {
                        hiddenMap.put(_hiddenFriend.value!![i].friendId, true)
                    }
                } else if (response.status == 200 && response.data.isEmpty()) {
                    _hiddenFriend.value = emptyList()
                } else {
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                Timber.d("Fail Response: $error")
            }
        }
    }

    fun changeHiddenStatue(friendId: Long, assignedProfileId: Long) {
        viewModelScope.launch {
            try {
                val request = FriendStatusRequest(
                    assignedProfileId = assignedProfileId,
                    isMarked = true,
                    isHidden = !(hiddenMap[friendId]!!),
                    isBlocked = true
                )

                val response = friendRepository.get().putFriendStatus(friendId, request)

                if (response.status == 200) {
                    _selectedFriend.value = response.data
                    hiddenMap[friendId] = !(hiddenMap[friendId]!!)
                } else {
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                Timber.d("Fail Response: $error")
            }
        }
    }
}
