package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.inviteroom.InviteRoomReq
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.network.response.friend.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/12
 *
 */
@HiltViewModel
class InviteRoomViewModel @Inject constructor(
    private val createChatRoomUseCase: CreateChatRoomUseCase
) : ViewModel() {
    val TAG: String = "로그"
    private var _friendList = MutableStateFlow<List<Friend>>(emptyList())
    private var _markFriendList = MutableStateFlow<List<Friend>>(emptyList())
    private var _selectedFriendList = MutableStateFlow<List<Friend>>(emptyList())

    private var friendAdapter: InviteRoomAdapter? = null
    private var markAdapter: InviteRoomMarkAdapter? = null

    data class SelectedTable(var id: String, var position: Int, var adapterFlag: Int)
    private var selectedMap = HashMap<String, SelectedTable>()

    // private val uiState : StateFlow<UiState<>>
    private val _uiEvent = MutableStateFlow<UiState<Any>>(UiState.Loading)
    val uiEvent = _uiEvent.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    var friendList = _friendList.asStateFlow()
        get() = _friendList

    var markFriendList = _markFriendList.asStateFlow()
        get() = _markFriendList

    var selectedFriendList = _selectedFriendList.asStateFlow()
        get() = _selectedFriendList

    init {
        viewModelScope.launch {
            initFriendList().collect {
                _friendList.value = it
            }
        }
        viewModelScope.launch {
            initMarkList().collect {
                _markFriendList.value = it
            }
        }
    }

    fun initFriendList(): Flow<List<Friend>> = flow {
        var List: MutableList<Friend> = arrayListOf()

        var friend: Friend
        for (i in 0 until 20) {
            if (i < 2) {
            } else {
                friend = Friend("$i", "$i", "더미$i", 0)
                List.add(friend)
            }

            emit(List)
        }
    }

    fun initMarkList(): Flow<List<Friend>> = flow {

        var markList: MutableList<Friend> = arrayListOf()
        var friend: Friend

        for (i in 0 until 2) {
            friend = Friend("$i", "$i", "더미$i", 1)
            markList.add(friend)
        }
        emit(markList)
    }

    fun addFriendList(friend: Friend, position: Int, adapter: InviteRoomAdapter) {
        var list: List<Friend>
        if (!selectedMap.containsKey(friend.id)) {

            var selectedTable = SelectedTable(friend.id, position, 0)
            selectedMap.put(friend.id, selectedTable)
            adapter.putActivate(position)
            list = _selectedFriendList.value.map { it.copy() }
            var newList = list.toMutableList()
            newList.add(friend)
            this.friendAdapter = adapter
            _selectedFriendList.value = newList
        } else {
            var newList: MutableList<Friend> = _selectedFriendList.value.toMutableList()
            newList.removeIf {
                it.id == friend.id
            }
            selectedMap.remove(friend.id)
            this.friendAdapter = adapter
            adapter.removeActivate(position)
            _selectedFriendList.value = newList
        }
    }

    fun addMarkList(friend: Friend, position: Int, adapter: InviteRoomMarkAdapter) {
        var list: List<Friend>
        if (!selectedMap.containsKey(friend.id)) {

            var selectedTable = SelectedTable(friend.id, position, 1)
            selectedMap.put(friend.id, selectedTable)
            adapter.putActivate(position)
            list = _selectedFriendList.value.map { it.copy() }
            var newList = list.toMutableList()
            newList.add(friend)
            this.markAdapter = adapter
            _selectedFriendList.value = newList
        } else {

            var newList: MutableList<Friend> = _selectedFriendList.value.toMutableList()
            newList.removeIf {
                it.id == friend.id
            }
            selectedMap.remove(friend.id)
            Log.d(TAG, "$adapter")
            this.markAdapter = adapter
            adapter.removeActivate(position)
            _selectedFriendList.value = newList
        }
    }

    fun removeSelectedItem(friend: Friend) {
        var newTable: SelectedTable = selectedMap.get(friend.id)!!
        var newPosition = newTable.position
        var newList: MutableList<Friend> = _selectedFriendList.value.toMutableList()
        newList.removeIf {
            it.id == friend.id
        }
        selectedMap.remove(friend.id)
        _selectedFriendList.value = newList
        if (newTable.adapterFlag == 0) {
            this.friendAdapter?.removeActivate(newPosition)
        } else {
            this.markAdapter?.removeActivate(newPosition)
        }
    }

    fun createRooms() {
        _uiState.value = UiState.Loading
        val inviteRoomReq = InviteRoomReq(
            hostId = "1643163512893324414", isOpen = false,
            users = listOf("1643163512893324414", "1643163512893324415")
        )
        viewModelScope.launch {
            createChatRoomUseCase.invoke(inviteRoomReq = inviteRoomReq)
                .collect {
                    result ->
                    when (result) {
                        is Results.Success ->
                            {
                                _uiState.value = UiState.Success(true)
                            }
                        is Results.Error ->
                            {
                                // _uiEvent.value = UiState.Success(false)
                                _uiState.value = UiState.Error("에러 발생")
                                null
                            }
                    }
                }
        }
    }

    fun replaceUiState() {
        _uiState.value = UiState.Loading
        _selectedFriendList.value = emptyList()
        selectedMap.clear()
    }
}
