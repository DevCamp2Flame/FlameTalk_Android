package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import androidx.lifecycle.*
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomListViewModel @Inject constructor(private val getChatRoomListUseCase: GetChatRoomListUseCase) :
    ViewModel() {
    val TAG: String = "로그"
    private val _uiState = MutableStateFlow<UiState<GetChatRoomListRes>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getChatRoomList()
    }

    fun getChatRoomList() {
        viewModelScope.launch {
            getChatRoomListUseCase.invoke(false)
                .collect { result ->
                    when (result) {
                        is Results.Success -> {
                            _uiState.value = UiState.Success(result.data)
                        }
                    }
                }
        }
    }
}
