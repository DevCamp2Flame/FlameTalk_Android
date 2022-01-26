package com.sgs.devcamp2.flametalk_android.ui.openchatroom

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgs.devcamp2.flametalk_android.data.dummy.*
import com.sgs.devcamp2.flametalk_android.data.model.openchat.MyOpenChatProfilePreview
import com.sgs.devcamp2.flametalk_android.data.model.openchat.MyOpenChatRoomPreview
import com.sgs.devcamp2.flametalk_android.data.model.openchat.OpenChatRoomPreview
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class OpenChatRoomViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyOpenChatRoomData: ArrayList<OpenChatRoomPreview> = getOpenChatRoom()
    private var dummyMyOpenChatRoomData: ArrayList<MyOpenChatRoomPreview> = getMyOpenChatRoom()
    private var dummyMyOpenChatProfileData: ArrayList<MyOpenChatProfilePreview> = getMyOpenProfile()

    // 친구 리스트
    private val _openChatRoom: MutableLiveData<ArrayList<OpenChatRoomPreview>> = MutableLiveData()
    val openChatRoom: MutableLiveData<ArrayList<OpenChatRoomPreview>> = _openChatRoom

    // 내 오픈채팅방 리스트
    private val _myOpenChatRoom: MutableLiveData<ArrayList<MyOpenChatRoomPreview>> =
        MutableLiveData()
    val myOpenChatRoom: MutableLiveData<ArrayList<MyOpenChatRoomPreview>> = _myOpenChatRoom

    // 내 오픈프로필 리스트
    private val _myOpenChatProfile: MutableLiveData<ArrayList<MyOpenChatProfilePreview>> =
        MutableLiveData()
    val myOpenChatProfile: MutableLiveData<ArrayList<MyOpenChatProfilePreview>> = _myOpenChatProfile

    init {
        _openChatRoom.value = dummyOpenChatRoomData
        _myOpenChatRoom.value = dummyMyOpenChatRoomData
        _myOpenChatProfile.value = dummyMyOpenChatProfileData
    }
}
