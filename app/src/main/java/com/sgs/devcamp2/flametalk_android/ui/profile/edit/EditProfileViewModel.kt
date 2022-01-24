package com.sgs.devcamp2.flametalk_android.ui.profile.edit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyUser
import com.sgs.devcamp2.flametalk_android.network.response.friend.ProfilePreview
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyUserData: ProfilePreview = getDummyUser()

    // 메인 유저 정보
    private val _userProfile: MutableStateFlow<ProfilePreview?> = MutableStateFlow(null)
    val userProfile: MutableStateFlow<ProfilePreview?> = _userProfile

    // 닉네임
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    // 프로필 상태메세지
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    // 프로필 이미지
    private val _profileImage = MutableStateFlow("")
    val profileImage = _profileImage.asStateFlow()

    // 프로필 이미지 url
    private val _profileImageUrl = MutableStateFlow("")
    val profileImageUrl = _profileImageUrl.asStateFlow()

    // 배경 이미지
    private val _backgroundImage = MutableStateFlow("")
    val backgroundImage = _backgroundImage.asStateFlow()

    // 배경 이미지 url
    private val _backgroundImageUrl = MutableStateFlow("")
    val backgroundImageUrl = _backgroundImageUrl.asStateFlow()

    fun getProfileDesc(desc: String) {
        _description.value = desc
    }

    fun setUserProfile(data: ProfilePreview) {
        _userProfile.value = data

        _nickname.value = _userProfile.value!!.nickname
        _description.value = _userProfile.value!!.description.toString()
        _profileImage.value = _userProfile.value!!.image.toString()
        _backgroundImage.value = _userProfile.value!!.backgroundImage.toString()
    }

    fun updateProfileDate() {
        val profile = ProfilePreview(
            _userProfile.value!!.userId,
            _userProfile.value!!.nickname,
            _profileImage.value,
            _description.value,
            _backgroundImage.value

        )
        // TODO: 통신 준비
    }

    fun setProfileImage(path: String?) {
        if (path != null) {
            _profileImage.value = path
        }
    }

    fun setBackgroundImage(path: String) {
        if (path != null) {
            _backgroundImage.value = path
        }
    }

    private fun pathToMultipartFile(url: String): MultipartBody.Part {
        val file = File(url)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            "multipartFile",
            file.name,
            requestFile
        )
    }

    fun postCreateImage(url: String) {
        val multipartFile = pathToMultipartFile(url)
    }
}
