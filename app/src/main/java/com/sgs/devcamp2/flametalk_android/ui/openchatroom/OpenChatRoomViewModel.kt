package com.sgs.devcamp2.flametalk_android.ui.openchatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.*
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist.GetChatRoomListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * @author 김현국
 * @created 2022/01/26
 */

@HiltViewModel
class OpenChatRoomViewModel @Inject constructor(
    private val getChatRoomListUseCase: GetChatRoomListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GetChatRoomListRes>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getOpenChatRoomList()
    }

    private fun getOpenChatRoomList() {
        viewModelScope.launch {
            getChatRoomListUseCase.invoke(true)
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
