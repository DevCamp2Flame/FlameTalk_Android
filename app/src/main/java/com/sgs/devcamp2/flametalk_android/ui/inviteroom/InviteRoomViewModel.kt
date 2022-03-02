package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToFriendEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.entity.inviteroom.FriendEntity
import com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom.GetFriendListUseCase
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
    private val getFriendListUseCase: GetFriendListUseCase
) : ViewModel() {
    val TAG: String = "로그"
    private val _friendList = MutableStateFlow<List<FriendEntity>>(emptyList())
    private val _selectedFriendList = MutableStateFlow<List<FriendEntity>>(emptyList())

    private var friendAdapter: InviteRoomAdapter? = null
    private var markAdapter: InviteRoomMarkAdapter? = null

    data class SelectedTable(var id: String, var position: Int, var adapterFlag: Int)

    private var selectedMap = HashMap<String, SelectedTable>()
    private val _uiEvent = MutableStateFlow<UiState<Any>>(UiState.Loading)
    val uiEvent = _uiEvent.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _friendListUiState = MutableStateFlow<UiState<List<FriendEntity>>>(UiState.Loading)
    val friendListUiState = _friendListUiState.asStateFlow()

    val friendList = _friendList.asStateFlow()

    val selectedFriendList = _selectedFriendList.asStateFlow()

    init {
    }
//    fun initFriendList(): Flow<List<FriendListRes>> = flow {
//        var List: MutableList<FriendListRes> = arrayListOf()
//
//        var tempFriend: FriendListRes
//        for (i in 0 until 20) {
//            if (i < 2) {
//            } else {
//                tempFriend = FriendListRes("$i", "$i", "더미$i", 0)
//                List.add(tempFriend)
//            }
//
//            emit(List)
//        }
//    }
//    fun initMarkList(): Flow<List<FriendListRes>> = flow {
//
//        var markList: MutableList<FriendListRes> = arrayListOf()
//        var tempFriend: FriendListRes
//
//        for (i in 0 until 2) {
//            tempFriend = FriendListRes("$i", "$i", "더미$i", 1)
//            markList.add(tempFriend)
//        }
//        emit(markList)
//    }
    /**
     * Map을 이용하여, 일반 친구를 선택하면 선택된 친구 리스트를 갱신하는 function입니다.
     */
    fun addFriendList(friendEntity: FriendEntity, position: Int, adapter: InviteRoomAdapter) {
        var list: List<FriendEntity>
        if (!selectedMap.containsKey(friendEntity.userId)) {
            var selectedTable = SelectedTable(friendEntity.userId, position, 0)
            selectedMap.put(friendEntity.userId, selectedTable)
            adapter.putActivate(position)
            list = _selectedFriendList.value.map { it.copy() }
            var newList = list.toMutableList()
            newList.add(friendEntity)
            this.friendAdapter = adapter
            _selectedFriendList.value = newList
        } else {
            var newList: MutableList<FriendEntity> = _selectedFriendList.value.toMutableList()
            newList.removeIf {
                it.userId == friendEntity.userId
            }
            selectedMap.remove(friendEntity.userId)
            this.friendAdapter = adapter
            adapter.removeActivate(position)
            _selectedFriendList.value = newList
        }
    }
    /**
     * Map을 이용하여, 일반 친구를 선택하면 선택된 친구 리스트를 갱신하는 function입니다.
     */
//    fun addMarkList(tempFriend: FriendListRes, position: Int, adapter: InviteRoomMarkAdapter) {
//        var list: List<FriendListRes>
//        if (!selectedMap.containsKey(tempFriend.id)) {
//
//            var selectedTable = SelectedTable(tempFriend.id, position, 1)
//            selectedMap.put(tempFriend.id, selectedTable)
//            adapter.putActivate(position)
//            list = _selectedFriendList.value.map { it.copy() }
//            var newList = list.toMutableList()
//            newList.add(tempFriend)
//            this.markAdapter = adapter
//            _selectedFriendList.value = newList
//        } else {
//
//            var newList: MutableList<FriendListRes> = _selectedFriendList.value.toMutableList()
//            newList.removeIf {
//                it.id == tempFriend.id
//            }
//            selectedMap.remove(tempFriend.id)
//            Log.d(TAG, "$adapter")
//            this.markAdapter = adapter
//            adapter.removeActivate(position)
//            _selectedFriendList.value = newList
//        }
//    }
    /**
     * selectedMap에 저장되어 있는 아이템을 클릭시
     * 해당 아이템이 어떤 recyclerview의 adapter에 있는 아이ㅁ인지
     */
    fun removeSelectedItem(friendEntity: FriendEntity) {
        var newTable: SelectedTable = selectedMap.get(friendEntity.userId)!!
        var newPosition = newTable.position
        var newList: MutableList<FriendEntity> = _selectedFriendList.value.toMutableList()
        newList.removeIf {
            it.userId == friendEntity.userId
        }
        selectedMap.remove(friendEntity.userId)
        _selectedFriendList.value = newList
        if (newTable.adapterFlag == 0) {
            this.friendAdapter?.removeActivate(newPosition)
        } else {
            this.markAdapter?.removeActivate(newPosition)
        }
    }

    fun getFriendList() {
        viewModelScope.launch {
            getFriendListUseCase.invoke(
                false, false, false
            ).collect { result ->
                when (result) {
                    is Results.Success -> {
                        val friendListEntity = result.data.map {
                            mapperToFriendEntity(it)
                        }
                        _friendListUiState.value = UiState.Success(friendListEntity)
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
