package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomReq
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.network.response.friend.TempFriend
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
    private var _friendList = MutableStateFlow<List<TempFriend>>(emptyList())
    private var _markFriendList = MutableStateFlow<List<TempFriend>>(emptyList())
    private var _selectedFriendList = MutableStateFlow<List<TempFriend>>(emptyList())

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

    fun initFriendList(): Flow<List<TempFriend>> = flow {
        var List: MutableList<TempFriend> = arrayListOf()

        var tempFriend: TempFriend
        for (i in 0 until 20) {
            if (i < 2) {
            } else {
                tempFriend = TempFriend("$i", "$i", "더미$i", 0)
                List.add(tempFriend)
            }

            emit(List)
        }
    }

    fun initMarkList(): Flow<List<TempFriend>> = flow {

        var markList: MutableList<TempFriend> = arrayListOf()
        var tempFriend: TempFriend

        for (i in 0 until 2) {
            tempFriend = TempFriend("$i", "$i", "더미$i", 1)
            markList.add(tempFriend)
        }
        emit(markList)
    }

    fun addFriendList(tempFriend: TempFriend, position: Int, adapter: InviteRoomAdapter) {
        var list: List<TempFriend>
        if (!selectedMap.containsKey(tempFriend.id)) {

            var selectedTable = SelectedTable(tempFriend.id, position, 0)
            selectedMap.put(tempFriend.id, selectedTable)
            adapter.putActivate(position)
            list = _selectedFriendList.value.map { it.copy() }
            var newList = list.toMutableList()
            newList.add(tempFriend)
            this.friendAdapter = adapter
            _selectedFriendList.value = newList
        } else {
            var newList: MutableList<TempFriend> = _selectedFriendList.value.toMutableList()
            newList.removeIf {
                it.id == tempFriend.id
            }
            selectedMap.remove(tempFriend.id)
            this.friendAdapter = adapter
            adapter.removeActivate(position)
            _selectedFriendList.value = newList
        }
    }

    fun addMarkList(tempFriend: TempFriend, position: Int, adapter: InviteRoomMarkAdapter) {
        var list: List<TempFriend>
        if (!selectedMap.containsKey(tempFriend.id)) {

            var selectedTable = SelectedTable(tempFriend.id, position, 1)
            selectedMap.put(tempFriend.id, selectedTable)
            adapter.putActivate(position)
            list = _selectedFriendList.value.map { it.copy() }
            var newList = list.toMutableList()
            newList.add(tempFriend)
            this.markAdapter = adapter
            _selectedFriendList.value = newList
        } else {

            var newList: MutableList<TempFriend> = _selectedFriendList.value.toMutableList()
            newList.removeIf {
                it.id == tempFriend.id
            }
            selectedMap.remove(tempFriend.id)
            Log.d(TAG, "$adapter")
            this.markAdapter = adapter
            adapter.removeActivate(position)
            _selectedFriendList.value = newList
        }
    }

    fun removeSelectedItem(tempFriend: TempFriend) {
        var newTable: SelectedTable = selectedMap.get(tempFriend.id)!!
        var newPosition = newTable.position
        var newList: MutableList<TempFriend> = _selectedFriendList.value.toMutableList()
        newList.removeIf {
            it.id == tempFriend.id
        }
        selectedMap.remove(tempFriend.id)
        _selectedFriendList.value = newList
        if (newTable.adapterFlag == 0) {
            this.friendAdapter?.removeActivate(newPosition)
        } else {
            this.markAdapter?.removeActivate(newPosition)
        }
    }

    fun createRooms() {

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
                                // UiState.Success(result.data)
                               // _uiEvent.value = UiState.Success(result.data)
                                _uiState.value = UiState.Success(true)
                            }
                        is Results.Error ->
                            {
                                //_uiEvent.value = UiState.Success(false)
                                _uiState.value = UiState.Error("에러 발생")
                                null
                            }
                    }
                }
        }
    }
}
