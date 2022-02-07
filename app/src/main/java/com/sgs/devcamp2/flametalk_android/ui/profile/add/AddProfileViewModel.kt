package com.sgs.devcamp2.flametalk_android.ui.profile.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.repository.FileRepository
import com.sgs.devcamp2.flametalk_android.network.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
import com.sgs.devcamp2.flametalk_android.util.pathToMultipartImageFile
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import timber.log.Timber

@HiltViewModel
class AddProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDAO: UserDAO,
    private val fileRepository: Lazy<FileRepository>,
    private val profileRepository: Lazy<ProfileRepository>
) : ViewModel() {

    // 유저 Id
    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    // 유저 닉네임
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

    // 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 메세지
    private val _isSuccess: MutableStateFlow<Boolean>? = null
    val isSuccess = _isSuccess?.asStateFlow()

    init {
        viewModelScope.launch {
            userDAO.user.collect {
                if (it != null) {
                    _userId.value = it.userId
                    _nickname.value = it.nickname
                }
            }
        }
    }

    fun setProfileDesc(desc: String) {
        _description.value = desc
        Timber.d("Description ${_description.value}")
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

    // 갤러리에서 가져온 프로필, 배경 이미지를 파일 서버로 보내고 파일서버 url을 받는다.
    fun addProfile() {
        viewModelScope.launch {
            if (_profileImage.value != "") {
                _profileImageUrl.value = postCreateImage(PROFILE_IMAGE)
            }

            if (_backgroundImage.value != "") {
                _backgroundImageUrl.value = postCreateImage(BACKGROUND_IMAGE)
            }
        }
        postProfile()
    }

    // File Create 통신
    private fun postCreateImage(type: Int): String {
        var multipartFile: MultipartBody.Part? = null
        var url: String = ""

        when (type) {
            PROFILE_IMAGE -> {
                multipartFile =
                    pathToMultipartImageFile(_profileImage.value, "image/*".toMediaTypeOrNull())
            }
            BACKGROUND_IMAGE -> {
                multipartFile =
                    pathToMultipartImageFile(_backgroundImage.value, "image/*".toMediaTypeOrNull())
            }
        }

        // 파일 서버로 이미지를 보내고 url path 리턴
        viewModelScope.launch {
            try {
                val response = fileRepository.get().postFileCreate(multipartFile!!, null)
                response.data.url

                if (response.status == 201) {
                    url = response.data.url
                } else {
                    _message.value = response.message
                }
                Timber.d("Success $response")
            } catch (ignore: Throwable) {
                Timber.d("Fail $ignore")
            }
        }
        return url
    }

    private fun postProfile() {
        viewModelScope.launch {
            try {
                val request = ProfileCreateRequest(
                    userId = _userId.value,
                    imageUrl = _profileImageUrl.value,
                    bgImageUrl = _backgroundImageUrl.value,
                    sticker = null,
                    description = _description.value,
                    isDefault = false
                )
                val response = profileRepository.get().createProfile(request)

                _isSuccess?.value = response.status == 200
                _message.value = response.message

                Timber.d("Success $response")
            } catch (ignore: Throwable) {
                Timber.d("Fail $ignore")
            }
        }
    }

    companion object {
        private const val PROFILE_IMAGE = 1
        private const val BACKGROUND_IMAGE = 2
    }
}
