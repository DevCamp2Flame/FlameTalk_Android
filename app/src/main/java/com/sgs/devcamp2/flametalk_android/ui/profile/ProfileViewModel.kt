package com.sgs.devcamp2.flametalk_android.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyUser
import com.sgs.devcamp2.flametalk_android.network.response.friend.ProfilePreview
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyUserData: ProfilePreview = getDummyUser()

    // 메인 유저 정보
    private val _userProfile = MutableStateFlow(dummyUserData)
    val userProfile = _userProfile.asStateFlow()

    init {
        _userProfile.value = dummyUserData
    }
}
