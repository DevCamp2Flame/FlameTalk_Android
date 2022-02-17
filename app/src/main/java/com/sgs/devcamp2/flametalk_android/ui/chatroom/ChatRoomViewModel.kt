package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatReq
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.closechatroom.CloseChatRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.uploadimg.UploadImgRes
import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.GetChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.*
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.SaveReceivedMessageUseCase
import com.sgs.devcamp2.flametalk_android.services.WebSocketListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/26
 */
@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val saveReceivedMessageUseCase: SaveReceivedMessageUseCase,
    private val getChatRoomInfoUseCase: GetChatRoomInfoUseCase,
    private val deleteChatRoomUseCase: DeleteChatRoomUseCase,
    private val closeChatRoomUseCase: CloseChatRoomUseCase,
    private val getChatListUseCase: GetChatListUseCase,
    private val getChatListHistoryUseCase: GetChatListHistoryUseCase,
    private val getLastReadMessageUseCase: GetLastReadMessageUseCase,
    private val upLoadImageUseCase: UpLoadImageUseCase,
    private val client: OkHttpClient,
    private val request: Request,
    private val webSocketListener: WebSocketListener,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val TAG: String = "로그"
    private var _chat = MutableStateFlow<String>("")
    var chat = _chat.asStateFlow()
    lateinit var _jsonStompSessions: StompSessionWithKxSerialization
    private var _drawUserState = MutableStateFlow<UiState<GetChatRoomEntity>>(UiState.Loading)

    var drawUserState = _drawUserState.asStateFlow()

    private var _deleteUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    var deleteUiState = _deleteUiState.asStateFlow()

    private var _closeUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    var closeUiState = _closeUiState.asStateFlow()

    private var _uploadUiState = MutableStateFlow<UiState<UploadImgRes>>(UiState.Loading)
    var uploadUiState = _uploadUiState.asStateFlow()

    private var _chatList = MutableStateFlow<UiState<List<Chat>>>(UiState.Loading)
    var chatList = _chatList.asStateFlow()

    private var _lastReadMessageId = MutableStateFlow("")
    var lastReadMessageId = _lastReadMessageId.asStateFlow()

    private var _uiState = MutableStateFlow<UiState<Long>>(UiState.Loading)
    var uiState = _uiState.asStateFlow()

    private var _userChatRoom = MutableStateFlow<UiState<ChatRoom>>(UiState.Loading)
    var userChatRoom = _userChatRoom.asStateFlow()

    private var _localChatRoom = MutableStateFlow<UiState<ChatRoom>>(UiState.Loading)
    var localChatRoom = _localChatRoom.asStateFlow()

    private var _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private var _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private var _roomId = MutableStateFlow("")
    val roomId = _roomId.asStateFlow()

    private val _imageUrl = MutableStateFlow<String>("")
    val imageUrl = _imageUrl.asStateFlow()

    private var webSocket: WebSocket? = null

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
    /**
     * 현재 사용자가 채팅방을 보고 있는지 상태 update하는 function입니다.
     */
    fun connectWebsocket(chatroomId: String, deviceId: String) {
        viewModelScope.launch {
            val gson = Gson()
            val map = HashMap<String, String>()
            map["type"] = "ENTER"
            map["userId"] = userId.value
            map["roomId"] = chatroomId
            map["deviceId"] = deviceId
            webSocket = client.newWebSocket(
                request,
                webSocketListener
            )
            webSocket?.send(gson.toJson(map))
            _roomId.value = chatroomId
        }
    }
    /**
     * 휴대폰 내부 DB에서 채팅 텍스트 리스트를 불러오는 fucntion입니다.
     */
    fun getChatList(chatroomId: String) {
        Log.d(TAG, "getChatList - $chatroomId")
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

    fun getChatListWithLastReadMessageId(chatroomId: String, lastReadMessageId: String) {
        viewModelScope.launch {
            getChatListHistoryUseCase.invoke(chatroomId, lastReadMessageId)
                .collectLatest { result ->
                    when (result) {
                        is Results.Success -> {
                            Log.d(TAG, "getChatListWithLastReadMesaageId - $chatroomId")
                            getChatList(chatroomId)
                        }
                    }
                }
        }
    }
    /**
     * 채팅방 서랍을 열었을 때 유저들의 정보를 가져오는 function입니다.
     */
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
    /**
     * 유저가 채팅방을 나가는 function 입니다.
     */
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
    /**
     * 유저가 채팅을 수신하는 function입니다.
     *
     * @param session mainViewModel에서 가져오는 stompSession
     * @param roomId 현재 보고 있는 채팅방 id
     * roomId에 해당하는 경로를 구독하며 수신받은 채팅 텍스트를 ChatRes data class 로 변환한다.
     * 채팅을 수신한 경우 사용자가 채팅을 읽었음을 처리하고 내부 데이터 베이스에 저장한다.
     */
    fun receivedMessage(session: StompSession, roomId: String) {
        viewModelScope.launch {
            _jsonStompSessions = session.withJsonConversions()
            val subscription: Flow<ChatRes> =
                _jsonStompSessions.subscribe("/sub/chat/room/$roomId", ChatRes.serializer())
            val collectorJob = launch {
                subscription.collect { msg ->
                    _lastReadMessageId.value = msg.message_id // 내가 읽은 메세지 초기화
                    saveReceivedMessageUseCase.invoke(msg).collect {
                        if (msg.message_type == "TALK" || msg.message_type == "FILE") {
                            _uiState.value = UiState.Success(it)
                        }
                    }
                }
            }
        }
    }
    /**
     * 유저가 채팅을 송신하는 function입니다.
     * @param messageType 사용자가 보내는 채팅 메시지의 type ( TALK, INVITE , ENTER , FILE )
     * 연결된 websocket session에 메세지를 전송한다.
     */
    fun pushMessage(messageType: String, roomId: String, session: StompSession, contents: String?, file_url: String?) {
        viewModelScope.launch {
            if (contents !== "") {
                _jsonStompSessions = session.withJsonConversions()
                // 만약 message type이 INVITE일 경우 한번 더 메세지를 보내자.
                if (messageType == "INVITE") {
                    val chatReq1 =
                        ChatReq(messageType, roomId, _userId.value, _nickname.value, contents, null)
                    val chatReq2 =
                        ChatReq("TALK", roomId, _userId.value, _nickname.value, contents, null)
                    _jsonStompSessions.convertAndSend(
                        "/pub/chat/message",
                        chatReq1,
                        ChatReq.serializer()
                    )
                    delay(500L)
                    _jsonStompSessions.convertAndSend(
                        "/pub/chat/message",
                        chatReq2,
                        ChatReq.serializer()
                    )
                } else if (messageType == "FILE") {
                    val chatReq3 = ChatReq("FILE", roomId, _userId.value, _nickname.value, null, file_url)
                    _jsonStompSessions.convertAndSend(
                        "/pub/chat/message",
                        chatReq3, ChatReq.serializer()
                    )
                } else {
                    val chatReq =
                        ChatReq(messageType, roomId, _userId.value, _nickname.value, contents, null)
                    _jsonStompSessions.convertAndSend(
                        "/pub/chat/message",
                        chatReq,
                        ChatReq.serializer()
                    )
                }
            }
        }
    }
    /**
     * 유저가 채팅방을 벗어났을 때, 사용자가 마지막으로 읽은 messageId를 초기화하는 function입니다.
     */
    fun closeChatRoom(userChatroomId: Long, lastReadMessageId: String) {
        viewModelScope.launch {
            val closeChatRoomReq = CloseChatRoomReq(userChatroomId, lastReadMessageId)
            closeChatRoomUseCase.invoke(closeChatRoomReq).collect { result ->
                when (result) {
                    is Results.Success -> {
                        _closeUiState.value = UiState.Success(true)
                    }
                }
            }
        }
    }

    fun updateState() {
        _deleteUiState.value = UiState.Loading
    }

    fun saveExitStatus(chatroomId: String, deviceId: String) {
        viewModelScope.launch {
            val gson = Gson()
            val map = HashMap<String, String>()
            map["type"] = "EXIT"
            map["userId"] = userId.value
            map["roomId"] = chatroomId
            map["deviceId"] = deviceId

            webSocket?.send(gson.toJson(map))
            delay(500L)
            webSocket?.cancel()
            onCleared()
        }
    }

    fun getLastReadMessageId(chatroomId: String) {
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

    fun uploadImage(multipartfile: MultipartBody.Part) {
        viewModelScope.launch {
            val roomIdBody: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), _roomId.value)
            upLoadImageUseCase.invoke(multipartfile, roomIdBody).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            Log.d(TAG, "ChatRoomViewModel - uploadImage() called")
                            _uploadUiState.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }
    fun initUploadImageState() {
        _uploadUiState.value = UiState.Loading
    }

    fun setBackgroundImage(path: String) {
        if (path != null) {
            _imageUrl.value = path
        }
    }

}
