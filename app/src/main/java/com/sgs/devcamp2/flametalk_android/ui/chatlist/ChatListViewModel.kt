package com.sgs.devcamp2.flametalk_android.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.domain.model.response.chatlist.ChattingRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor() : ViewModel() {

    val TAG: String = "로그"

    private var _chattingRoomList = MutableLiveData<MutableList<ChattingRoom>>()
    private val list = mutableListOf<ChattingRoom>()

    val chattingRoomList: LiveData<MutableList<ChattingRoom>>
        get() = _chattingRoomList

    init {
        viewModelScope.launch {
            initChattingRoom().collect {
                list.add(it)
                _chattingRoomList.value = list
            }
        }
    }

    fun initChattingRoom(): Flow<ChattingRoom> = flow {
        for (i in 0 until 100) {
            val userList = arrayListOf<String>("김현국", "김다롬", "박소연", "최수연")
            val chattingRoom = ChattingRoom(i, "flame$i", userList)
            emit(chattingRoom)
        }
    }
}
