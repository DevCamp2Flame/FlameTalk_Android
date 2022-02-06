package com.sgs.devcamp2.flametalk_android.ui.chatroom

import androidx.lifecycle.*
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.SaveReceivedMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val saveReceivedMessageUseCase: SaveReceivedMessageUseCase
) : ViewModel() {
    val TAG: String = "로그"
    private var _chatRoom = MutableStateFlow<List<Chat>>(emptyList())
    private var list = mutableListOf<Chat>()
    private var _chat = MutableStateFlow<String>("")
    var chat = _chat.asStateFlow()
    lateinit var _roomId: String
    lateinit var _jsonStompSessions: StompSessionWithKxSerialization
    /**
     * _chatUserList는 채팅방에 속해 있는 user의 정보를 의미한다. image url, 이름 등이 포함 될 수 있다.
     * Object 대신 String 으로 테스트한다.
     */
    private var _chatUserList = MutableLiveData<MutableList<String>>()
    private var userlist = mutableListOf<String>()
    val chatRoom = _chatRoom.asStateFlow()
    val userRoom: LiveData<MutableList<String>>
        get() = _chatUserList

    init {
        viewModelScope.launch {
            initChattingText().collect {
                list.add(it)
                _chatRoom.value = list
            }
            initChattingUser().collect {
                userlist.add(it)
                _chatUserList.value = userlist
            }
        }
    }

    fun initChattingText(): Flow<Chat> = flow {
        val randomNumber = Random()
        val textList = arrayListOf<String>(
            "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
            "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.",
            "Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. "
        )
        for (i in 0 until 4) {
            val j = randomNumber.nextInt(3)
            val k = randomNumber.nextInt(2)
            val chatText = Chat("$i", "1", "$k", "${textList[j]}", "gd")
            emit(chatText)
        }
    }

    fun initChattingUser(): Flow<String> = flow {
        for (i in 0 until 20) {
            emit("김현국 $i")
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

    fun initRoom(roomId: String) {
        _roomId = roomId
    }

    fun receivedMessage(session: StompSession, roomId: String) {
        GlobalScope.launch {
            _jsonStompSessions = session.withJsonConversions()
            val subscription: Flow<ChatEntity> =
                _jsonStompSessions.subscribe("/sub/chat/room/$roomId", ChatEntity.serializer())
            val collectorJob = launch {
                subscription.collect { msg ->
                    // Log.d(TAG, "receivedMessage - $msg")
                    saveReceivedMessageUseCase.invoke(msg)
                }
            }
        }
    }

    fun pushMessage(roomId: String, session: StompSession, contents: String) {
        viewModelScope.launch {
            // sender id -> user_id 로 변경하고
            // nickname도 변경
            val chatReq = ChatEntity("talk", "", "user_id", "nick_name", contents)
            _jsonStompSessions = session.withJsonConversions()
            _jsonStompSessions.convertAndSend("/pub/chat/message", chatReq, ChatEntity.serializer())
        }
    }
}
