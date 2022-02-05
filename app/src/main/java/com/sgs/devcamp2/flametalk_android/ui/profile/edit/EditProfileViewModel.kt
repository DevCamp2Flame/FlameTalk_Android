package com.sgs.devcamp2.flametalk_android.ui.profile.edit

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyUser
import com.sgs.devcamp2.flametalk_android.network.repository.FileRepository
import com.sgs.devcamp2.flametalk_android.network.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileUpdateRequest
import com.sgs.devcamp2.flametalk_android.data.model.ProfilePreview
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileRepository: Lazy<FileRepository>,
    private val profileRepository: Lazy<ProfileRepository>
) : ViewModel() {
    // 네트워크 통신 데이터 전 더미데이터
    private var dummyUserData: ProfilePreview = getDummyUser()
    val TAG: String = "로그"
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
            "file", file.name, requestFile
        )
    }


    // File Create 통신
    fun postCreateImage() {
        val multipartFile = pathToMultipartFile(_profileImage.value)

        // 테스트용 더미 api
        viewModelScope.launch {
            try {

                // fileRepository.get().getCreatedFile(7) -> get
                // fileRepository.get().deleteCreatedFile(7) -> delete
                var response = fileRepository.get().postFileCreate(multipartFile)
                Log.d(TAG, "EditProfileViewModel - $response")
            } catch (ignore: Throwable) {
                Timber.d("EditProfileViewModel: 알 수 없는 에러 발생")
            }
        }
    }

    fun addProfile() {
        viewModelScope.launch {
            try {
                val request = ProfileCreateRequest(
                    userId = "1643610416180811276",
                    imageUrl = _profileImage.value,
                    bgImageUrl = _backgroundImage.value,
                    sticker = null,
                    description = _description.value,
                    isDefault = false
                )
                // TODO: profile, background 이미지 생성 통신 후 await으로 진행
                val response = profileRepository.get().createProfile(request)
                Timber.d(response.toString())
            } catch (ignore: Throwable) {
                Timber.d("알 수 없는 에러 발생")
            }
        }
    }

    fun updateProfileData(profileId: Long, isDefault: Boolean) {
        val request = ProfileUpdateRequest(
            userId = "유저아이디를 똑바로 저장했어야 했는데",
            imageUrl = _profileImageUrl.value,
            bgImageUrl = _backgroundImageUrl.value,
            sticker = null, // TODO: 스티커 정보, 프로필 내 positioning 할 수 있는 상대적 위치 정보
            description = _description.value,
            isDefault = isDefault
        )
        viewModelScope.launch {
            try {
                val response = profileRepository.get().updateProfile(profileId, request)
                Timber.d(response.toString())
            } catch (ignored: Throwable) {
                Timber.d("알 수 없는 에러 발생")
            }
        }
    }
}
