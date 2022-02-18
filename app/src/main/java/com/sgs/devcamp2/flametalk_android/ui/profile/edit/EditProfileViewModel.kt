package com.sgs.devcamp2.flametalk_android.ui.profile.edit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.Sticker
import com.sgs.devcamp2.flametalk_android.data.model.profile.Profile
import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
import com.sgs.devcamp2.flametalk_android.domain.repository.FileRepository
import com.sgs.devcamp2.flametalk_android.domain.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileUpdateRequest
import com.sgs.devcamp2.flametalk_android.util.pathToMultipartImageFile
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import timber.log.Timber

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences,
    private val fileRepository: Lazy<FileRepository>,
    private val profileRepository: Lazy<ProfileRepository>
) : ViewModel() {

    // 유저 id
    private val _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    // 프로필 id
    private val _profileId = MutableStateFlow(0L)
    val profileId = _profileId.asStateFlow()

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

    // 스티커
    private val _stickers = MutableStateFlow<List<Sticker>>(emptyList())
    val stickers = _stickers.asStateFlow()

    // 배경 이미지 url
    private val _isDefault: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isDefault = _isDefault.asStateFlow()

    // 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    // 메세지
    private val _isSuccess: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isSuccess = _isSuccess.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.user.collect {
                if (it != null) {
                    _userId.value = it.userId
                    _nickname.value = it.nickname
                }
            }
        }
    }

    fun setProfileDesc(desc: String) {
        _description.value = desc
    }

    fun setUserProfile(data: Profile) {
        _profileId.value = data.profileId
        if (data.description == null) {
            _description.value = ""
        } else {
            _description.value = data.description.toString()
        }
        _profileImageUrl.value = data.imageUrl.toString()
        _backgroundImageUrl.value = data.bgImageUrl.toString()
        _isDefault.value = data.isDefault
        if (data.sticker == null) {
            _stickers.value = emptyList()
        } else {
            _stickers.value = data.sticker!!
        }
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

    fun updateProfile() {
        viewModelScope.launch {
            val deferred = viewModelScope.async {
                // list 내의 작업을 비동기적으로 요청하고 모든 요청이 완료될 때 까지 기다린다.
                val postUploadFiles = listOf(
                    async {
                        if (_profileImage.value != "") {
                            postCreateImage(PROFILE_IMAGE)
                        }
                    },
                    async {
                        if (_backgroundImage.value != "") {
                            postCreateImage(BACKGROUND_IMAGE)
                        }
                    }
                )
                postUploadFiles.awaitAll()
            }
            // 파일 통신이 모두 끝나면 프로필 생성 요청을 보낸다
            deferred.await()
            updateProfileData()
        }
    }

    // File Create 통신
    private suspend fun postCreateImage(type: Int) {
        // 파일 서버로 이미지를 보내고 url path 리턴
        val deferred = viewModelScope.async {
            val multipartFile: MultipartBody.Part? = when (type) {
                PROFILE_IMAGE -> {
                    pathToMultipartImageFile(_profileImage.value, "image/*".toMediaTypeOrNull())
                }
                BACKGROUND_IMAGE -> {
                    pathToMultipartImageFile(_backgroundImage.value, "image/*".toMediaTypeOrNull())
                }
                else -> { // 실행되지 않음
                    null
                }
            }

            try {
                val response = fileRepository.get().postFileCreate(multipartFile!!, null)

                if (response.status == 200) {
                    if (type == PROFILE_IMAGE) {
                        _profileImageUrl.value = response.data.url
                    } else if (type == BACKGROUND_IMAGE) {
                        _backgroundImageUrl.value = response.data.url
                    }
                } else {
                    _message.value = response.message
                }
            } catch (ignore: Throwable) {
                Timber.d("Fail $ignore")
            }
        }
        deferred.await()
    }

    //
    fun updateProfileData() {
        val request = ProfileUpdateRequest(
            userId = _userId.value,
            imageUrl = _profileImageUrl.value,
            bgImageUrl = _backgroundImageUrl.value,
            sticker = emptyList(), // TODO: 스티커 정보, 프로필 내 positioning 할 수 있는 상대적 위치 정보
            description = _description.value,
            isDefault = _isDefault.value!!
        )
        viewModelScope.launch {
            try {
                val response = profileRepository.get().updateProfile(_profileId.value, request)

                if (response.status == 200) {
                    _isSuccess.value = true
                } else {
                    _message.value = response.message
                }
                Timber.d(response.toString())
            } catch (ignored: Throwable) {
                Timber.d("알 수 없는 에러 발생")
            }
        }
    }

    companion object {
        private const val PROFILE_IMAGE = 1
        private const val BACKGROUND_IMAGE = 2
    }
}
