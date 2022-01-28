package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import androidx.lifecycle.*
import com.sgs.devcamp2.flametalk_android.domain.model.ChatRoomList
import com.sgs.devcamp2.flametalk_android.domain.model.Results
import com.sgs.devcamp2.flametalk_android.domain.model.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomListViewModel @Inject constructor(private val getChatRoomListUseCase: GetChatRoomListUseCase) : ViewModel() {

    val TAG: String = "로그"

    private val _uiState = MutableStateFlow<UiState<List<ChatRoomList>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
    }

    suspend fun getChatRoomList(user_id: String) {
        getChatRoomListUseCase.getChatRoomList(user_id).mapNotNull {
            result ->
            when (result) {
                is Results.Success ->
                    {
                        _uiState.value = UiState.Success(result.data)
                    }
            }
        }
    }
}
