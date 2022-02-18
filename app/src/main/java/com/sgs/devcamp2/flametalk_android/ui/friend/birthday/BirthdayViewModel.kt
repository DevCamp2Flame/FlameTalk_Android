package com.sgs.devcamp2.flametalk_android.ui.friend.birthday

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend
import com.sgs.devcamp2.flametalk_android.data.model.friend.FriendStatus
import com.sgs.devcamp2.flametalk_android.domain.repository.FriendRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/02/18
 * @desc 샘일인 친구 리스트.
 */

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val friendRepository: Lazy<FriendRepository>
) : ViewModel() {

    // 숨김 친구 리스트
    private val _birthdayFriend: MutableStateFlow<List<Friend>?> = MutableStateFlow(null)
    val birthdayFriend: MutableStateFlow<List<Friend>?> = _birthdayFriend

    // 선택된 친구
    private val _selectedFriend: MutableStateFlow<FriendStatus?> = MutableStateFlow(null)
    val selectedFriend: MutableStateFlow<FriendStatus?> = _selectedFriend

    // 유저에게 피드백 해야하는 에러 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 디버그용 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        getBirthdayFriendList()
    }

    // 생일인 친구 리스트 요청
    private fun getBirthdayFriendList() {
        viewModelScope.launch {
            try {
                val response = friendRepository.get().getFriendList(true, null, null)

                // 생일 친구가 있는 경우
                if (response.status == 200 && response.data.isNotEmpty()) {
                    _birthdayFriend.value = response.data
                } else if (response.status == 200 && response.data.isEmpty()) {
                    _birthdayFriend.value = emptyList()
                } else {
                    _message.value = response.message
                }
            } catch (error: Throwable) {
                Timber.d("Fail Response: $error")
            }
        }
    }
}
