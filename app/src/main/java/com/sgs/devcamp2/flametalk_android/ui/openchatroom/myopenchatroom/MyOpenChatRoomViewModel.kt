package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenchatroom

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgs.devcamp2.flametalk_android.data.dummy.getMyOpenChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.openchat.MyOpenChatRoomPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/26
 */
@HiltViewModel
class MyOpenChatRoomViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private var dummyMyOpenChatRoomData: ArrayList<MyOpenChatRoomPreview> = getMyOpenChatRoom()
    // 내 오픈채팅방 리스트
    private val _myOpenChatRoom: MutableLiveData<ArrayList<MyOpenChatRoomPreview>> =
        MutableLiveData()
    val myOpenChatRoom: MutableLiveData<ArrayList<MyOpenChatRoomPreview>> = _myOpenChatRoom
    init {
        _myOpenChatRoom.value = dummyMyOpenChatRoomData
    }
}
