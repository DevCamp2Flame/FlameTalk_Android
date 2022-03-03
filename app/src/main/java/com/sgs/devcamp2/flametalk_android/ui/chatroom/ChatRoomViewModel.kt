package com.sgs.devcamp2.flametalk_android.ui.chatroom

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
    private val getUserChatRoomIdUseCase: GetUserChatRoomIdUseCase,
    private val client: OkHttpClient,
    private val request: Request,
    private val webSocketListener: WebSocketListener,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val TAG: String = "로그"
    private val _chat = MutableStateFlow<String>("")
    val chat = _chat.asStateFlow()
    lateinit var _jsonStompSessions: StompSessionWithKxSerialization

    private val _drawUserState = MutableStateFlow<UiState<GetChatRoomEntity>>(UiState.Loading)
    val drawUserState = _drawUserState.asStateFlow()

    private val _deleteUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val deleteUiState = _deleteUiState.asStateFlow()

    private val _closeUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val closeUiState = _closeUiState.asStateFlow()

    private val _uploadUiState = MutableStateFlow<UiState<UploadImgRes>>(UiState.Loading)
    val uploadUiState = _uploadUiState.asStateFlow()

    private val _chatList = MutableStateFlow<UiState<List<Chat>>>(UiState.Loading)
    val chatList = _chatList.asStateFlow()

    val _lastReadMessageId = MutableStateFlow("")
    val lastReadMessageId = _lastReadMessageId.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<Long>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _userChatRoom = MutableStateFlow<UiState<ChatRoom>>(UiState.Loading)
    val userChatRoom = _userChatRoom.asStateFlow()

    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    private val _roomId = MutableStateFlow("")
    val roomId = _roomId.asStateFlow()

    private val _userChatRoomId = MutableStateFlow(0L)
    val userChatRoomId
        get() = _userChatRoomId
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

    fun getUserChatRoomId(chatroomId: String) {
        viewModelScope.launch {
            getUserChatRoomIdUseCase.invoke(chatroomId).collect { result ->
                when (result) {
                    is LocalResults.Success -> {
                        _userChatRoomId.value = result.data
                    }
                    is LocalResults.Error -> {
                        _userChatRoomId.value = 0L
                    }
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
        viewModelScope.launch {
            getChatListUseCase.invoke(chatroomId).collect { result ->
                when (result) {
                    is LocalResults.Success -> {
                        _chatList.value = UiState.Success(result.data.chatList)
                        _userChatRoom.value = UiState.Success(result.data.room)
                        _userChatRoomId.value = result.data.room.userChatroomId
                    }
                    is LocalResults.Error -> {
                        _chatList.value = UiState.Loading
                        _userChatRoom.value = UiState.Loading
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
                            getChatList(chatroomId)
                        }
                        is Results.Error -> {
                            getChatList(chatroomId)
                        }
                        is Results.Loading ->
                            {
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
                        _drawUserState.value = UiState.Error(result.message)
                    }
                    is Results.Loading -> {
                        _drawUserState.value = UiState.Loading
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
                    is Results.Loading ->
                        {
                            _deleteUiState.value = UiState.Loading
                        }
                    is Results.Error ->
                        {
                            _deleteUiState.value = UiState.Error(result.message)
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
    /**
     * 유저가 채팅을 송신하는 function입니다.
     * @param messageType 사용자가 보내는 채팅 메시지의 type ( TALK, INVITE , ENTER , FILE )
     * 연결된 websocket session에 메세지를 전송한다.
     */
    fun pushMessage(
        messageType: String,
        roomId: String,
        session: StompSession,
        contents: String?,
        file_url: String?
    ) {
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
                    val chatReq3 =
                        ChatReq("FILE", roomId, _userId.value, _nickname.value, null, file_url)
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
                    is Results.Loading ->
                        {
                            _closeUiState.value = UiState.Loading
                        }
                    is Results.Error ->
                        {
                            _closeUiState.value = UiState.Error(result.message)
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
        }
    }

    fun getLastReadMessageId(chatroomId: String) {
        viewModelScope.launch {
            getLastReadMessageUseCase.invoke(chatroomId).collect { result ->
                when (result) {
                    is LocalResults.Success -> {

                        if (result.data == "null") {
                            _lastReadMessageId.value = ""
                        } else {
                            _lastReadMessageId.value = result.data
                        }
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
            val roomIdBody: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), _roomId.value)
            upLoadImageUseCase.invoke(multipartfile, roomIdBody).collect { result ->
                when (result) {
                    is Results.Success -> {
                        _uploadUiState.value = UiState.Success(result.data)
                    }
                    is Results.Error ->
                        {
                            _uploadUiState.value = UiState.Error(result.message)
                        }
                    is Results.Loading ->
                        {
                            _uploadUiState.value = UiState.Loading
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
