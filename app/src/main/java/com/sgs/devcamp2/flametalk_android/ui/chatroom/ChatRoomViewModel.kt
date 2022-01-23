package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.network.request.ChatMessageDto
import com.sgs.devcamp2.flametalk_android.network.response.chat.Chat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    val TAG: String = "로그"

    private var _chatRoom = MutableStateFlow<List<Chat>>(emptyList())

    private var list = mutableListOf<Chat>()

    private var _chat = MutableStateFlow<String>("")

    val okHttpClient = OkHttpClient.Builder()
        .callTimeout(Duration.ofMinutes(1))
        .pingInterval(Duration.ofSeconds(10))
        .build()
    val wsClient = OkHttpWebSocketClient(okHttpClient)
    val stompClient = StompClient(wsClient)
    var url = "ws://10.99.30.180:8080/stomp/chat"

    /**
     * _chatUserList는 채팅방에 속해 있는 user의 정보를 의미한다. image url, 이름 등이 포함 될 수 있다.
     * Object 대신 String 으로 테스트한다.
     */
    private var _chatUserList = MutableLiveData<MutableList<String>>()
    private var userlist = mutableListOf<String>()

    val chatRoom = _chatRoom.asStateFlow()

    val userRoom: LiveData<MutableList<String>>
        get() = _chatUserList

    val chat: StateFlow<String>
        get() = _chat

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

        viewModelScope.launch {
            connection().collect {

                var Req: ChatMessageDto = ChatMessageDto("a9024424-e88f-4e6b-b5d6-bd3db709ba87", "김현국", "안녕하세요 ")
                val jsonStompSession = it.withJsonConversions()
                jsonStompSession.convertAndSend("/pub/chat/enter", Json.encodeToString(Req))
            }
        }
    }

    fun connection(): Flow<StompSession> = flow {
        emit(stompClient.connect(url))
    }

    fun initChattingText(): Flow<Chat> = flow {

        val randomNumber = Random()
        val textList = arrayListOf<String>(
            "orem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English.",
            "Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. "
        )
        for (i in 0 until 4) {
            val j = randomNumber.nextInt(3)
            val k = randomNumber.nextInt(2)
            val chatText = Chat(i, "1", "$k", "${textList[j]}")
            emit(chatText)
        }
    }

    /**
     * 채팅방 유저 생성
     */

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

    fun sendMessage() {
        // var chat = Chat(1, "1", "0", _chat.value)
        // addChatting(chat)
    }

}
