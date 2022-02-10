package com.sgs.devcamp2.flametalk_android.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyFriend
import com.sgs.devcamp2.flametalk_android.data.model.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyFriendData: List<Friend> = getDummyFriend()

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
        _searchedFriend.value = dummyFriendData
    }
}
