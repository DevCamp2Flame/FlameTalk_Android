package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.domain.model.response.friend.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
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
     * [position]  inviteRoomAdapter와 inviteRoomMarkAdatper에서의 item position
     */
    fun addMarkFriendToMap(friend: Friend, position: Int) {

        if (inviteFriendMap?.containsKey(friend.id) == true) {

            inviteFriendMap?.remove(friend.id)
            inviteList.remove(friend)
            _inviteFriendList.value = inviteList
            if (friend.mark == 1) {
                markList[position].selected = 0
                _friendMarkList.value = markList
            } else {

                list[position].selected = 0
                _friendList.value = list
            }
        } else {

            inviteFriendMap?.put(friend.id, position)
            if (friend.mark == 1) {
                markList[position].selected = 1
                _friendMarkList.value = markList
                inviteList.add(friend)

                _inviteFriendList.value = inviteList
            } else {
                list[position].selected = 1
                _friendList.value = list
                inviteList.add(friend)
                _inviteFriendList.value = inviteList
            }
        }
    }
    /**
     * [position] inviteFriendList에서의 position
     */

    fun removeSelectedItem(friend: Friend, position: Int) {
        var removePosition = inviteFriendMap?.get(friend.id)
        inviteFriendMap?.remove(friend.id)
        inviteList.removeAt(position)

        _inviteFriendList.value = inviteList
        if (friend.mark == 1) {
            markList[removePosition!!].selected = 0
            _friendMarkList.value = markList
        } else {
            list[removePosition!!].selected = 0
            _friendList.value = list
        }
    }
}
