package com.sgs.devcamp2.flametalk_android.ui.createchatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.CreateChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/08
 */
@HiltViewModel
class CreateChatRoomViewModel @Inject constructor(
    val createChatRoomUseCase: CreateChatRoomUseCase
) : ViewModel() {
    private var _firstMessage = ""

    private var _createUiState = MutableStateFlow<UiState<CreateChatRoomEntity>>(UiState.Loading)
    var createUiState = _createUiState.asStateFlow()
    fun updateFirstMessage(input: String) {
        _firstMessage = input
    }
    fun createChatRoom(users: List<String>) {
        val createChatRoomReq = CreateChatRoomReq(
            hostId = "1643986912282658350",
            hostOpenProfileId = null,
            isOpen = false,
            users = users,
            title = null,
            thumbnail = null
        )
        viewModelScope.launch {
            createChatRoomUseCase.invoke(createChatRoomReq).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _createUiState.value = UiState.Success(result.data)
                        }
                    is Results.Error ->
                        {
                        }
                }
            }
        }
    }
    fun pushFirstMessage() {
        viewModelScope.launch {
            //
        }
    }

    fun updateRoomInfo(){

    }
}
