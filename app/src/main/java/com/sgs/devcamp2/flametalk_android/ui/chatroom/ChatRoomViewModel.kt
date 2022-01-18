package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.network.response.chat.Chat
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    val TAG: String = "로그"

    private var _chatRoom = MutableLiveData<MutableList<Chat>>()
    private var list = mutableListOf<Chat>()

    /**
     * _chatUserList는 채팅방에 속해 있는 user의 정보를 의미한다. image url, 이름 등이 포함 될 수 있다.
     * Object 대신 String 으로 테스트한다.
     */
    private var _chatUserList = MutableLiveData<MutableList<String>>()
    private var userlist = mutableListOf<String>()

    val chatRoom: LiveData<MutableList<Chat>>
        get() = _chatRoom

    val userRoom: LiveData<MutableList<String>>
        get() = _chatUserList

    init {
        viewModelScope.launch {
            initChattingText().collect {
                list.add(it)
                _chatRoom.value = list
                Log.d(TAG, "_chatRoom - ${_chatRoom.value}() called")
            }
            initChattingUser().collect {
                userlist.add(it)
                _chatUserList.value = userlist
            }
        }
    }

    /**
     * itemView를 나누기 위해서 총 4가지의 변수를 두어야한다.
     * leftstart , left, rightstart, right
     *
     * 사용자의 userid를 받아서 비교해서 같으면 오른쪽, 아니라면 왼쪽
     * 내가 아닌 다른 사용자가 첫번째로 보낸 메세지면 leftStart ViewHolder에 전달
     * 위의 메세지 이후에 오는 메시지는 leftViewHolder에 전달
     * >> 첫번째 보넀을 때는 이름과 사진이 보여야하지만 연달아서 오는 메시지는 텍스트만 보여주고 , 날짜가 옮겨간다.
     */

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
        var list = _chatRoom.value?.map { it.copy() }?.toMutableList()
        list?.add(chat)
        Log.d(TAG, "list - $list")
        _chatRoom.value = list!!
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ChatRoomViewModel - onCleared() called")
    }
}
