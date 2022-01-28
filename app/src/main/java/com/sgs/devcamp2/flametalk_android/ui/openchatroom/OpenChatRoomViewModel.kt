package com.sgs.devcamp2.flametalk_android.ui.openchatroom

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgs.devcamp2.flametalk_android.data.dummy.*
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

    // 친구 리스트
    private val _openChatRoom: MutableLiveData<ArrayList<OpenChatRoomPreview>> = MutableLiveData()
    val openChatRoom: MutableLiveData<ArrayList<OpenChatRoomPreview>> = _openChatRoom

    init {
        _openChatRoom.value = dummyOpenChatRoomData
    }
}
