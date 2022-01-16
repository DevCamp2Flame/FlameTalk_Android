package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.domain.model.response.friend.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/12
 *
 */
@HiltViewModel
class InviteRoomViewModel @Inject constructor() : ViewModel() {
    val TAG: String = "로그"
    private var _friendMarkList = MutableLiveData<MutableList<Friend>>()
    private val markList = mutableListOf<Friend>()
    private var _friendList = MutableLiveData<MutableList<Friend>>()
    private val list = mutableListOf<Friend>()
    private var inviteFriendMap: HashMap<Int, Int>? = HashMap()

    private var _inviteFriendList = MutableLiveData<MutableList<Friend>>()
    private val inviteList = mutableListOf<Friend>()
    val friendMarkList: LiveData<MutableList<Friend>>
        get() = _friendMarkList
    val friendList: LiveData<MutableList<Friend>>
        get() = _friendList
    val inviteFriendList: LiveData<MutableList<Friend>>
        get() = _inviteFriendList

    init {
        viewModelScope.launch {

            initFriendList().collect {
                if (it.mark == 1) {
                    markList.add(it)
                    _friendMarkList.value = markList
                } else {
                    list.add(it)
                    _friendList.value = list
                }
            }
        }
    }

    fun initFriendList(): Flow<Friend> = flow {
        for (i in 0 until 20) {
            if (i < 2) {
                val friend = Friend(i, "$i", "더미$i", 1, 0)
                emit(friend)
            } else {
                val friend = Friend(i, "$i", "더미$i", 0, 0)
                emit(friend)
            }
        }
    }
    /**
     * addMarkFriendToMap
     * [position]  inviteRoomAdapter와 inviteRoomMarkAdatper에서의 item position
     * [GlobalScope] MutableLiveData<MutableList<Friend>> = deepCopyList.toMutableList()가 실행되면
     * observe되어 있기 때문에 adapter로 전송되어 diffUtil발동 및 변경사항 바인드
     * 바인드되기 이전, Observe되고 있는 list들이 업데이트가 되면 diffUtil이 변경점을 찾지 못함
     * 따라서 CoroutineScope를 이용하여 delay를 주어 bind이후 list가 업데이트되도록 함.
     */
    fun addMarkFriendToMap(friend: Friend, position: Int) {

        var deepCopyList: List<Friend>
        var deepInviteList: List<Friend>

        if (inviteFriendMap?.containsKey(friend.id) == true) {

            inviteFriendMap?.remove(friend.id)
            deepCopyList = inviteList.map { it.copy() }

            var posFromInviteList = deepCopyList.indexOf(friend)
            var mutableDeepCopyList = deepCopyList.toMutableList() // deepCopyList는 list라 remove가 안됌
            mutableDeepCopyList.removeAt(posFromInviteList)

            _inviteFriendList.value = mutableDeepCopyList
            inviteList.removeAt(posFromInviteList)

            if (friend.mark == 1) {
                deepCopyList = markList.map { it.copy() }
                deepCopyList[position].selected = 0
                _friendMarkList.value = deepCopyList.toMutableList()

                GlobalScope.launch {
                    delay(100)
                    markList[position].selected = 0
                }
            } else {
                deepCopyList = list.map { it.copy() }
                deepCopyList[position].selected = 0
                _friendList.value = deepCopyList.toMutableList()
                GlobalScope.launch {
                    delay(100)
                    list[position].selected = 0
                }
            }
        } else {

            inviteFriendMap?.put(friend.id, position)
            deepInviteList = inviteList.map { it.copy() }.toMutableList()
            if (friend.mark == 1) {
                deepCopyList = markList.map { it.copy() }
                deepCopyList[position].selected = 1
                _friendMarkList.value = deepCopyList.toMutableList()
                GlobalScope.launch {
                    delay(100)
                    markList[position].selected = 1
                }
            } else {
                deepCopyList = list.map { it.copy() }
                deepCopyList[position].selected = 1
                _friendList.value = deepCopyList.toMutableList()
                GlobalScope.launch {
                    delay(100)
                    list[position].selected = 1
                }
            }
            deepInviteList.add(deepCopyList[position])
            _inviteFriendList.value = deepInviteList
            inviteList.add(deepCopyList[position])
        }
    }
    fun removeSelectedItem(friend: Friend) {
        var removePosition = inviteFriendMap?.get(friend.id)
        var deepInviteCopyList = inviteList.map { it.copy() }.toMutableList()
        inviteFriendMap?.remove(friend.id)
        var pos = deepInviteCopyList.indexOf(friend)
        deepInviteCopyList.removeAt(pos)
        _inviteFriendList.value = deepInviteCopyList.toMutableList()
        GlobalScope.launch {
            delay(100)
            inviteList.removeAt(pos)
        }
        var deepCopyList: List<Friend>

        if (friend.mark == 1) {
            deepCopyList = markList.map { it.copy() }
            deepCopyList[removePosition!!].selected = 0
            _friendMarkList.value = deepCopyList.toMutableList()
            // markList[removePosition].selected = 0
            GlobalScope.launch {
                delay(100)
                markList[removePosition].selected = 0
            }
        } else {
            deepCopyList = list.map { it.copy() }
            deepCopyList[removePosition!!].selected = 0
            _friendList.value = deepCopyList.toMutableList()
            // list[removePosition].selected = 0
            GlobalScope.launch {
                delay(100)
                list[removePosition].selected = 0
            }
        }
    }
}
