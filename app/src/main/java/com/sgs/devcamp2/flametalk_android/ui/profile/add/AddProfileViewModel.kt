package com.sgs.devcamp2.flametalk_android.ui.profile.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.Sticker
import com.sgs.devcamp2.flametalk_android.data.source.local.UserPreferences
import com.sgs.devcamp2.flametalk_android.domain.repository.FileRepository
import com.sgs.devcamp2.flametalk_android.domain.repository.ProfileRepository
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileCreateRequest
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

/**
 * @author 박소연
 * @created 2022/01/25
 * @updated 2022/02/17
 * @desc 프로필 생성 관련 비즈니스 로직 수행
 *       파일 생성 통신, url 변환, 스티커 생성, 스티커 생성, 프로필 생성 툥산
 */

@HiltViewModel
class AddProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferences: UserPreferences,
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
    private val _profileImageUrl: MutableStateFlow<String?> = MutableStateFlow(null)

    // 배경 이미지
    private val _backgroundImage = MutableStateFlow("")
    val backgroundImage = _backgroundImage.asStateFlow()

    // 배경 이미지 url
    private val _backgroundImageUrl: MutableStateFlow<String?> = MutableStateFlow(null)

    // 스티커
    var stickers = arrayListOf<Sticker>()

    // 파일 통신 결과
    private val _isFileSuccess: MutableStateFlow<Boolean?> = MutableStateFlow(null)

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

    // image uri를 path로 전환
    @SuppressLint("Range")
    fun uriToPath(activity: Activity, uri: Uri): String {
        val cursor = activity.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToNext()
        val path: String? = cursor?.getString(cursor.getColumnIndex("_data"))
        cursor?.close()

        if (path.isNullOrEmpty()) {
            return ""
        }
        return path
    }

    // 갤러리에서 가져온 프로필, 배경 이미지를 파일 서버로 보내고 파일서버 url을 받는다.
    fun addProfile() {
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
            if (_isFileSuccess.value == true) {
                postProfile()
            }
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
                else -> {
                    null
                }
            }

            try {
                val response = fileRepository.get().postFileCreate(multipartFile!!, null)

                when (response.status) {
                    200 -> {
                        if (type == PROFILE_IMAGE) {
                            _profileImageUrl.value = response.data.url
                        } else if (type == BACKGROUND_IMAGE) {
                            _backgroundImageUrl.value = response.data.url
                        }
                        _isFileSuccess.value = true
                    }
                    400 -> {
                        _message.value = "10MB가 넘는 파일은 업로드할 수 없습니다."
                        _isFileSuccess.value = false
                    }
                    else -> {
                        _message.value = response.message
                    }
                }
            } catch (ignore: Throwable) {
                Timber.d("Fail $ignore")
            }
        }
        deferred.await()
    }

    fun createSticker(id: Int, stickerType: Int, x: Double, y: Double) {
        /**프로필 조회하는 디바이스의 사이즈에 따라 scaling 하기 위해
         디바이스의 기기 가로, 세로 사이즈로 나누어 position 저장*/
        val dm: DisplayMetrics = context.resources.displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels

        // TODO: 추가된 id가 기존에 있을 경우 중복 추가가 아닌 갱신되어야 함
        val stickerModel = Sticker(
            stickerId = stickerType,
            positionX = x / width,
            positionY = y / height
        )
        stickers.add(stickerModel)
    }

    fun removeSticker(id: Int) {
        stickers.removeIf { it.stickerId == id }
    }

    // 프로필 생성 통신
    private fun postProfile() {
        viewModelScope.launch {
            try {
                val request = ProfileCreateRequest(
                    userId = _userId.value,
                    imageUrl = _profileImageUrl.value,
                    bgImageUrl = _backgroundImageUrl.value,
                    sticker = stickers.toList(),
                    description = _description.value,
                    isDefault = false
                )
                val response = profileRepository.get().createProfile(request)

                if (response.status == 201) {
                    _isSuccess.value = true
                }
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
