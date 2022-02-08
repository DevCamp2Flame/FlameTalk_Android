package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.util.Log
import androidx.lifecycle.*
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity
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
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val saveReceivedMessageUseCase: SaveReceivedMessageUseCase,
    private val getChatRoomInfoUseCase: GetChatRoomInfoUseCase,
    private val deleteChatRoomUseCase: DeleteChatRoomUseCase,
    private val closeChatRoomUseCase: CloseChatRoomUseCase,
    private val getChatListUseCase: GetChatListUseCase,
    private val pushChatUseCase: PushChatUseCase
) : ViewModel() {
    val TAG: String = "로그"
    private var _chatRoom = MutableStateFlow<List<Chat>>(emptyList())
    private var list = mutableListOf<Chat>()
    private var _chat = MutableStateFlow<String>("")
    var chat = _chat.asStateFlow()
    /**
     * _chatUserList는 채팅방에 속해 있는 user의 정보를 의미한다. image url, 이름 등이 포함 될 수 있다.
     * Object 대신 String 으로 테스트한다.
     */

    val chatRoom = _chatRoom.asStateFlow()
    lateinit var _roomId: String
    lateinit var _jsonStompSessions: StompSessionWithKxSerialization
    private var _drawUserState = MutableStateFlow<UiState<GetChatRoomEntity>>(UiState.Loading)
    var drawUserState = _drawUserState.asStateFlow()
    private var _deleteUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    var deleteUiState = _deleteUiState.asStateFlow()
    private var _chatList = MutableStateFlow<UiState<List<Chat>>>(UiState.Loading)
    var chatList = _chatList.asStateFlow()

    private var _lastReadMessageId = MutableStateFlow("")
    var lastReadMessageId = _lastReadMessageId.asStateFlow()

    fun getChatList(chatroomId: String) {
        viewModelScope.launch {
            getChatListUseCase.invoke(chatroomId).collect {
                result ->
                when (result) {
                    is LocalResults.Success ->
                        {
                            _chatList.value = UiState.Success(result.data.chatList)
                        }
                }
            }
        }
    }
    fun getChatRoomDetail(userChatroomId: Long) {
        viewModelScope.launch {
            getChatRoomInfoUseCase.invoke(userChatroomId).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _drawUserState.value = UiState.Success(result.data)
                        }
                    is Results.Error ->
                        {
                        }
                }
            }
        }
    }

    fun pushMessage(chatEntity: ChatEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                pushChatUseCase.invoke(chatEntity)
            }
        }
    }
    fun addChatting(chat: Chat) {
        var newList = _chatRoom.value.toMutableList()
        newList.add(chat)
        _chatRoom.value = newList
    }

    fun updateTextValue(text: String) {
        _chat.value = text
    }
    fun updateLastReadMessage(input : String)
    {
        _lastReadMessageId.value = input
    }

    fun initRoom(roomId: String) {
        _roomId = roomId
    }

    fun deleteChatRoom(userChatroomId: Long) {
        viewModelScope.launch {
            deleteChatRoomUseCase.invoke(userChatroomId).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _deleteUiState.value = UiState.Success(true)
                        }
                }
            }
        }
    }
    fun receivedMessage(session: StompSession, roomId: String) {
        viewModelScope.launch {
            _jsonStompSessions = session.withJsonConversions()
            val subscription: Flow<Chat> =
                _jsonStompSessions.subscribe("/sub/chat/room/$roomId", Chat.serializer())
            val collectorJob = launch {
                subscription.collect { msg ->
                     _lastReadMessageId.value = msg.sender_id //내가 읽은 메세지 초기화
                    saveReceivedMessageUseCase.invoke(msg)
                }
            }
        }
    }

    fun pushMessage(roomId: String, session: StompSession, contents: String) {
        viewModelScope.launch {
            // sender id -> user_id 로 변경하고
            // nickname도 변경
            val chatReq = ChatEntity("talk", "1643986912282658350", roomId, contents)
            _jsonStompSessions = session.withJsonConversions()
            _jsonStompSessions.convertAndSend("/pub/chat/message", chatReq, ChatEntity.serializer())
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
