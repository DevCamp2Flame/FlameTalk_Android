package com.sgs.devcamp2.flametalk_android.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.domain.entity.FriendModel
import com.sgs.devcamp2.flametalk_android.domain.repository.FriendRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val friendRepository: Lazy<FriendRepository>
) : ViewModel() {

    // 유저 id
    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    // 검색된 친구 리스트
    private val _searchedFriend = MutableStateFlow<List<FriendModel>>(emptyList())
    val searchedFriend: MutableStateFlow<List<FriendModel>> = _searchedFriend

    // 전체 친구 리스트
    private var _allFriends = MutableStateFlow<List<FriendModel>>(emptyList())
    val allFriends: MutableStateFlow<List<FriendModel>> = _allFriends

    // 유저에게 피드백 해야하는 에러 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 디버그용 에러
    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    init {
        // 뷰모델 생성 시 친구 전체 목록 가져옴
        viewModelScope.launch {
            friendRepository.get().getAllFriends().collectLatest {
                _allFriends.value = it
            }
        }
    }

    // 검색어 입력 후 이벤트 날릴 때 호출
    fun searchFriend(input: String) {
        var result: ArrayList<FriendModel> = arrayListOf()

        if (_allFriends.value.isNullOrEmpty()) {
            _message.value = "친구 데이터가 없습니다."
        } else {
            if (!input.isNullOrEmpty()) {
                _allFriends.value!!.map {
                    if (it.nickname.contains(input)) {
                        result.add(it)
                    }
                }
            } else {
            }
        }
        _searchedFriend.value = result
    }
}
