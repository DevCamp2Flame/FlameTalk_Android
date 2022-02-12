package com.sgs.devcamp2.flametalk_android.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyFriend
import com.sgs.devcamp2.flametalk_android.data.model.friend.Friend
import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
import com.sgs.devcamp2.flametalk_android.network.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import dagger.Lazy
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences,
    private val friendRepository: Lazy<FriendRepository>
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyFriendData: List<Friend> = getDummyFriend()

    // 유저 id
    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    // 전체 친구 리스트
    private val _allFriends = MutableStateFlow<List<Friend>?>(emptyList())
    val allFriends: MutableStateFlow<List<Friend>?> = _allFriends

    // 검색된 친구 리스트
    private val _searchedFriend = MutableStateFlow<List<Friend>?>(emptyList())
    val searchedFriend: MutableStateFlow<List<Friend>?> = _searchedFriend

    // 유저에게 피드백 해야하는 에러 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 디버그용 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        viewModelScope.launch {
            // 유저 아이디 가져옴
            userPreferences.user.collect {
                if (it != null) {
                    _userId.value = it.userId
                }
            }

            // 뷰모델 생성 시 친구 전체 목록 가져옴
            // _allFriends.value = friendRepository.get().getAllFriends(_userId.value)
        }
        // _allFriends.value = dummyFriendData
    }

    // 검색어 입력 후 이벤트 날릴 때 호출
    fun searchFriend(input: String) {
        val result: ArrayList<Friend> = arrayListOf()

        if (_allFriends.value.isNullOrEmpty()) {
            _message.value = "친구 데이터가 없습니다."
        } else {
            _allFriends.value!!.map {
                if (it.nickname.contains(input)) {
                    result.add(it)
                }
            }
        }
        _searchedFriend.value = result
    }
}
