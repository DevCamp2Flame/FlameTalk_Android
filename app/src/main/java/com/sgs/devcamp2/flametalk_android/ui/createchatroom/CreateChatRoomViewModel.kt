package com.sgs.devcamp2.flametalk_android.ui.createchatroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.mapper.mappeToUserChatRoomModel
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.UserChatRoom
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.ChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompSession
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/08
 */
@HiltViewModel
class CreateChatRoomViewModel @Inject constructor(
    val createChatRoomUseCase: CreateChatRoomUseCase,
) : ViewModel() {
    var firstMessage = ""
    lateinit var session: StompSession
    private var _createUiState = MutableStateFlow<UiState<ChatRoomEntity>>(UiState.Loading)
    var createUiState = _createUiState.asStateFlow()

    private var _successPushUiState = MutableStateFlow<UiState<Long>>(UiState.Loading)
    val successPushUiState = _successPushUiState.asStateFlow()

    var createdRoomInfo: UserChatRoom? = null

    fun updateFirstMessage(input: String) {
        firstMessage = input
    }
    fun createChatRoom(users: List<String>) {
        val createChatRoomReq = CreateChatRoomReq(
            hostId = "1643986912282658350",
            hostOpenProfileId = null,
            isOpen = false,
            // users = users,
            users = listOf("1643986912282658350", "1644196296080985280"),
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
                            Log.d("로그","CreateChatRoomViewModel - createChatRoom() called")
                        }
                }
            }
        }
    }
    fun updateRoomInfo(chatRoomEntity: ChatRoomEntity) {
        this.createdRoomInfo = mappeToUserChatRoomModel(chatRoomEntity)
    }
}
