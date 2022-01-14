package com.sgs.devcamp2.flametalk_android.ui.inviteroom

import android.util.Log
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
    private var inviteFriendMap: HashMap<Int, String>? = HashMap()
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
            /*initFriendList().collect {
                list.add(it)
                _friendList.value = list
            }
            initMarkFriendList().collect {
                markList.add(it)
                _friendMarkList.value = markList
            }*/

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

/*

    fun initFriendList(): Flow<Friend> = flow {
        for (i in 5 until 20) {
            val friend = Friend(i, "$i", "더미$i", false)
            emit(friend)
        }
    }

    fun initMarkFriendList(): Flow<Friend> = flow {
        for (i in 0 until 4) {
            val friend = Friend(i, "$i", "더미$i", false)
            emit(friend)
        }
    }*/

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

    fun addMarkFriendToMap(friend: Friend) {
        // map에 선택한 인원들을 넣는다.
        if (inviteFriendMap?.containsKey(friend.id) == true) {
            inviteFriendMap?.remove(friend.id)
            inviteList.remove(friend)
            Log.d(TAG, "InviteRoomViewModel - addMarkFriendToMap() inviteList : $inviteList")
        } else {

            inviteFriendMap?.put(friend.id, friend.nickname)
            inviteList.add(friend)
            Log.d(TAG, "InviteRoomViewModel - addMarkFriendToMap() inviteList : $inviteList")
        }
        _inviteFriendList.value = inviteList
        Log.d(TAG, "InviteRoomViewModel - addMarkFriendToMap() inviteList : $inviteList")
    }
}
// 라이브 데이터 객체.
