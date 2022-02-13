package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomReq
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom.CreateChatRoomUseCase
import com.sgs.devcamp2.flametalk_android.network.response.friend.TempFriend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author 김현극
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
    /**
     * Map을 이용하여, 일반 친구를 선택하면 선택된 친구 리스트를 갱신하는 function입니다.
     */
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
    /**
     * Map을 이용하여, 일반 친구를 선택하면 선택된 친구 리스트를 갱신하는 function입니다.
     */
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
    /**
     * selectedMap에 저장되어 있는 아이템을 클릭시
     * 해당 아이템이 어떤 recyclerview의 adapter에 있는 아이ㅁ인지
     */

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



    fun replaceUiState() {
        _uiState.value = UiState.Loading
        _selectedFriendList.value = emptyList()
        selectedMap.clear()
    }
}
