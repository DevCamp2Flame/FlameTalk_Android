package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.util.Log
import androidx.lifecycle.*
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatReq
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.closechatroom.CloseChatRoomReq
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.GetChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.*
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.SaveReceivedMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val saveReceivedMessageUseCase: SaveReceivedMessageUseCase,
    private val getChatRoomInfoUseCase: GetChatRoomInfoUseCase,
    private val deleteChatRoomUseCase: DeleteChatRoomUseCase,
    private val closeChatRoomUseCase: CloseChatRoomUseCase,
    private val getChatListUseCase: GetChatListUseCase,
) : ViewModel() {
    val TAG: String = "로그"
    private var _chat = MutableStateFlow<String>("")
    var chat = _chat.asStateFlow()
    lateinit var _jsonStompSessions: StompSessionWithKxSerialization
    private var _drawUserState = MutableStateFlow<UiState<GetChatRoomEntity>>(UiState.Loading)
    var drawUserState = _drawUserState.asStateFlow()
    private var _deleteUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    var deleteUiState = _deleteUiState.asStateFlow()
    private var _chatList = MutableStateFlow<UiState<List<Chat>>>(UiState.Loading)
    var chatList = _chatList.asStateFlow()
    private var _lastReadMessageId = MutableStateFlow("")
    var lastReadMessageId = _lastReadMessageId.asStateFlow()
    private var _uiState = MutableStateFlow<UiState<Long>>(UiState.Loading)
    var uiState = _uiState.asStateFlow()

    private var _userChatRoom = MutableStateFlow<UiState<ChatRoom>>(UiState.Loading)
    var userChatRoom = _userChatRoom.asStateFlow()



    fun getChatList(chatroomId: String) {
        viewModelScope.launch {
            getChatListUseCase.invoke(chatroomId).collect { result ->
                when (result) {
                    is LocalResults.Success -> {
                        _chatList.value = UiState.Success(result.data.chatList)
                        _userChatRoom.value = UiState.Success(result.data.room)

                    }
                }
            }
        }
    }

    fun getChatRoomDetail(userChatroomId: Long) {
        viewModelScope.launch {
            getChatRoomInfoUseCase.invoke(userChatroomId).collect { result ->
                when (result) {
                    is Results.Success -> {
                        _drawUserState.value = UiState.Success(result.data)
                    }
                    is Results.Error -> {
                    }
                }
            }
        }
    }

    fun updateTextValue(text: String) {
        _chat.value = text
    }

    fun updateLastReadMessage(input: String) {
        _lastReadMessageId.value = input
    }

    fun deleteChatRoom(userChatroomId: Long) {
        viewModelScope.launch {
            deleteChatRoomUseCase.invoke(userChatroomId).collect { result ->
                when (result) {
                    is Results.Success -> {
                        _deleteUiState.value = UiState.Success(true)
                    }
                }
            }
        }
    }

    fun receivedMessage(session: StompSession, roomId: String) {
        viewModelScope.launch {
            _jsonStompSessions = session.withJsonConversions()
            val subscription: Flow<ChatRes> =
                _jsonStompSessions.subscribe("/sub/chat/room/$roomId", ChatRes.serializer())
            val collectorJob = launch {
                subscription.collect { msg ->
                    _lastReadMessageId.value = msg.sender_id // 내가 읽은 메세지 초기화
                    Log.d(TAG, "msg - $msg() called")
                    saveReceivedMessageUseCase.invoke(msg).collect {
                        // state 변경  pushstate겠다
                        Log.d(TAG, "msg - $it() called")
                        _uiState.value = UiState.Success(it)
                    }
                }
            }
        }
    }

    fun disconnectStomp() {
        viewModelScope.launch {
        }
    }

    fun pushMessage(messageType: String, roomId: String, session: StompSession, contents: String) {
        viewModelScope.launch {
            // sender id -> user_id 로 변경하고
            // nickname도 변경
            _jsonStompSessions = session.withJsonConversions()
            // 만약 message type이 INVITE일 경우 한번 더 메세지를 보내자.
            val chatReq = ChatReq(messageType, roomId, "1643986912282658350", "닉네임", contents, null)
            _jsonStompSessions.convertAndSend("/pub/chat/message", chatReq, ChatReq.serializer())
        }
    }

    fun closeChatRoom(userChatroomId: Long, lastReadMessageId: String) {
        viewModelScope.launch {
            val closeChatRoomReq = CloseChatRoomReq(userChatroomId, lastReadMessageId)
            closeChatRoomUseCase.invoke(closeChatRoomReq).collect {
                Log.d(TAG, "WrappedResponse - $it() called")
            }
        }
    }
}
