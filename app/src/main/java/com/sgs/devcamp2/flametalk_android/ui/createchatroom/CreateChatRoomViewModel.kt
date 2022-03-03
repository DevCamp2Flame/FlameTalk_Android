package com.sgs.devcamp2.flametalk_android.ui.createchatroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToUserChatRoomModel
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.UserChatRoom
import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
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
 * @author 김현국
 * @created 2022/02/08
 */
@HiltViewModel
class CreateChatRoomViewModel @Inject constructor(
    val createChatRoomUseCase: CreateChatRoomUseCase,
    val userPreferences: UserPreferences
) : ViewModel() {
    var firstMessage = ""

    lateinit var session: StompSession

    private val _createUiState = MutableStateFlow<UiState<ChatRoomEntity>>(UiState.Loading)
    val createUiState = _createUiState.asStateFlow()

    private val _successPushUiState = MutableStateFlow<UiState<Long>>(UiState.Loading)
    val successPushUiState = _successPushUiState.asStateFlow()

    var createdRoomInfo: UserChatRoom? = null

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.user.collect {
                if (it != null) {
                    _userId.value = it.userId
                    _nickname.value = it.nickname
                }
            }
        }
    }
    fun updateFirstMessage(input: String) {
        firstMessage = input
    }
    /**
     * 선택한 친구들로 채팅방을 생성하는 function입니다.
     */
    fun createChatRoom(users: List<String>) {
        val userList = users.toMutableList()
        userList.add(_userId.value)
        val createChatRoomReq = CreateChatRoomReq(
            hostId = _userId.value,
            hostOpenProfileId = null,
            isOpen = false,
            users = userList,
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
                            _createUiState.value = UiState.Error(result.message)
                        }
                }
            }
        }
    }
    fun updateRoomInfo(chatRoomEntity: ChatRoomEntity) {
        this.createdRoomInfo = mapperToUserChatRoomModel(chatRoomEntity)
    }
}
