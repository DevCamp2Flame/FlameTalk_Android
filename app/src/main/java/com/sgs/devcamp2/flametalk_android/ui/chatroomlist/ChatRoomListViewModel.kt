package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.domain.model.response.chatlist.ChatList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomListViewModel @Inject constructor() : ViewModel() {

    val TAG: String = "로그"

    private var _chatList = MutableLiveData<MutableList<ChatList>>()
    private val list = mutableListOf<ChatList>()

    val chatList: LiveData<MutableList<ChatList>>
        get() = _chatList

    /**
     * 초기화 시 initChattingRoom 에서 emit된 chatRoom 하나씩 list 에 추가 후
     * livedata에 초기
     */
    init {
        viewModelScope.launch {
            initChattingRoom().collect {
                list.add(it)
                _chatList.value = list
            }
        }
    }

    /**
     * chatList 객체 생성 100개 생성 후 1개씩 emit
     */
    fun initChattingRoom(): Flow<ChatList> = flow {
        for (i in 0 until 100) {
            val userList = arrayListOf<String>("김현국", "김다롬", "박소연", "최수연")
            val chatroom = ChatList(i, "flame$i", user_list = userList, title = "", user_id = "")
            emit(chatroom)
        }
    }
}
