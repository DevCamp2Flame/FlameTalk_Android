package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgs.devcamp2.flametalk_android.data.dummy.getMyOpenProfile
import com.sgs.devcamp2.flametalk_android.data.model.openchat.MyOpenChatProfilePreview
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/26
 */
@HiltViewModel
class MyOpenChatProfileViewModel @Inject constructor() : ViewModel() {
    private var dummyMyOpenChatProfileData: ArrayList<MyOpenChatProfilePreview> = getMyOpenProfile()

    // 내 오픈프로필 리스트
    private val _myOpenChatProfile: MutableLiveData<ArrayList<MyOpenChatProfilePreview>> =
        MutableLiveData()
    val myOpenChatProfile: MutableLiveData<ArrayList<MyOpenChatProfilePreview>> = _myOpenChatProfile

    init {
        _myOpenChatProfile.value = dummyMyOpenChatProfileData
    }
}
