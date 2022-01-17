package com.sgs.devcamp2.flametalk_android.ui.friend

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sgs.devcamp2.flametalk_android.data.repository.UserRepository
import com.sgs.devcamp2.flametalk_android.domain.dummy.getBirthdayFriend
import com.sgs.devcamp2.flametalk_android.domain.dummy.getDummyFriend
import com.sgs.devcamp2.flametalk_android.domain.dummy.getDummyUser
import com.sgs.devcamp2.flametalk_android.domain.dummy.getMultiProfile
import com.sgs.devcamp2.flametalk_android.domain.model.response.friend.ProfilePreview
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*

@HiltViewModel
class FriendViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    userRepository: UserRepository
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyUserData: ProfilePreview = getDummyUser()
    private var dummyMultiProfileData: ArrayList<ProfilePreview> = getMultiProfile()
    private var dummyBirthdayData: ArrayList<ProfilePreview> = getBirthdayFriend()
    private var dummyFriendData: ArrayList<ProfilePreview> = getDummyFriend()

    // 메인 유저 정보
    private val _userProfile = MutableStateFlow(dummyUserData)
    val userProfile = _userProfile.asStateFlow()
    // 유저 멀티프로필
    private val _multiProfile: MutableLiveData<ArrayList<ProfilePreview>> = MutableLiveData()
    val multiProfile: MutableLiveData<ArrayList<ProfilePreview>> = _multiProfile
    // 생일인 친구 리스트
    private val _birthProfile: MutableLiveData<ArrayList<ProfilePreview>> = MutableLiveData()
    val birthProfile: MutableLiveData<ArrayList<ProfilePreview>> = _birthProfile
    // 친구 리스트
    private val _friendProfile: MutableLiveData<ArrayList<ProfilePreview>> = MutableLiveData()
    val friendProfile: MutableLiveData<ArrayList<ProfilePreview>> = _friendProfile

    init {
        _userProfile.value = dummyUserData
        _multiProfile.value = dummyMultiProfileData
        _birthProfile.value = dummyBirthdayData
        _friendProfile.value = dummyFriendData
    }

//    fun getUserData(userId: Int) {
//        // TODO: ListData 역할을 하는 StateFlow 객체에  dummyData를 담고 View에서 Observe하자
//    }
}
