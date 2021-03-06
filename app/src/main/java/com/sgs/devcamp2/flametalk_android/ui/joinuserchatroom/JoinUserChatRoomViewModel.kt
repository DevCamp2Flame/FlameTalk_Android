package com.sgs.devcamp2.flametalk_android.ui.joinuserchatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.inviteroom.FriendListRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatroom.JoinChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatroom.JoinChatRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.GetFriendListUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.GetLastReadMessageUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.JoinUserChatRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/17
 */
@HiltViewModel
class JoinUserChatRoomViewModel @Inject constructor(
    private val joinUserChatRoomUseCase: JoinUserChatRoomUseCase,
    private val getLastReadMessageUseCase: GetLastReadMessageUseCase,
    private val getFriendListUseCase: GetFriendListUseCase
) : ViewModel() {

    private val _lastReadMessageId = MutableStateFlow("")
    val lastReadMessageId = _lastReadMessageId.asStateFlow()

    private val _joinUiState = MutableStateFlow<UiState<JoinChatRoomRes>>(UiState.Loading)
    val joinUiState = _joinUiState.asStateFlow()

    private val _friendListUiState = MutableStateFlow<UiState<List<FriendListRes>>>(UiState.Loading)
    val friendListUiState = _friendListUiState.asStateFlow()

    val _userId = MutableStateFlow("")

    val TAG: String = "로그"

    init {
    }

    fun joinUser(chatroomId: String, userId: String, lastReadMessage: String) {
        val joinChatRoomReq = JoinChatRoomReq(chatroomId, userId, lastReadMessage, null)
        viewModelScope.launch {
            joinUserChatRoomUseCase.invoke(joinChatRoomReq).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _joinUiState.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }
    fun getLastReadMessageId(chatroomId: String, userId: String) {
        _userId.value = userId
        viewModelScope.launch {
            getLastReadMessageUseCase.invoke(chatroomId).collect { result ->
                when (result) {
                    is LocalResults.Success -> {
                        _lastReadMessageId.value = result.data
                    }
                    is LocalResults.Error -> {
                        _lastReadMessageId.value = ""
                    }
                }
            }
        }
    }
    fun getFriendList() {
        viewModelScope.launch {
            getFriendListUseCase.invoke(
                false, false, false
            ).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            Log.d(TAG, "JoinUserChatRoomViewModel - getFriendList() called")
                            _friendListUiState.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }
}
