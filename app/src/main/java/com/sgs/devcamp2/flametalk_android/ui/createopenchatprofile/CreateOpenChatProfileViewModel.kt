package com.sgs.devcamp2.flametalk_android.ui.createopenchatprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.createopenchatprofile.CreateOpenProfileUseCase
import com.sgs.devcamp2.flametalk_android.network.dao.UserDAO
import com.sgs.devcamp2.flametalk_android.network.repository.FileRepository
import com.sgs.devcamp2.flametalk_android.util.pathToMultipartImageFile
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.lang.Exception
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/03
 */
@HiltViewModel
class CreateOpenChatProfileViewModel @Inject constructor(
    private val createOpenProfileUseCase: CreateOpenProfileUseCase,
    private val userDAO: UserDAO,
    private val fileRepository: Lazy<FileRepository>
) : ViewModel() {
    private var _profile_name = MutableStateFlow("")
    val profile_name = _profile_name.asStateFlow()
    private var _profile_description = MutableStateFlow("")
    val profile_description = _profile_description.asStateFlow()

    private var _profile_image = MutableStateFlow("")
    val profile_image = _profile_image.asStateFlow()

    private var _profileImageUrl = MutableStateFlow("")
    val profileImageUrl = _profileImageUrl.asStateFlow()

    private var _uiState = MutableStateFlow<UiState<Boolean>>(UiState.Init)
    val uiState = _uiState.asStateFlow()

    private var _userId = MutableStateFlow("")
    val userId = _userId.asStateFlow()

    private var _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

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
    fun updateName(input: String) {
        _profile_name.value = input
    }

    fun updateDescription(input: String) {
        _profile_description.value = input
    }

    fun setProfileImage(path: String) {
        if (path != null) {
            _profile_image.value = path
        }
    }
    /**
     * 오픈 프로필을 생성하는 function 입니다.
     */
    fun addOpenProfile() {
        viewModelScope.launch {
            val deferred = viewModelScope.async {
                val openProfileImage =
                    async {
                        createOpenProfile()
                    }
                openProfileImage.await()
            }
            deferred.await()
            postOpenProfile()
        }
    }
    suspend fun createOpenProfile() {

        val deferred = viewModelScope.async {
            val multipartFile: MultipartBody.Part? = pathToMultipartImageFile(_profile_image.value, "image/*".toMediaTypeOrNull())
            try {
                val response = fileRepository.get().postFileCreate(multipartFile!!, null)
                if (response.status == 200) {
                    _profileImageUrl.value = response.data.url
                }
            } catch (e: Exception) {
            }
        }
        deferred.await()
    }
    fun postOpenProfile() {
        viewModelScope.launch {
            var createOpenChatProfileReq = CreateOpenProfileReq(
                _userId.value,
                _profile_name.value,
                _profileImageUrl.value,
                _profile_description.value
            )
            _uiState.value = UiState.Loading

            createOpenProfileUseCase.invoke(createOpenChatProfileReq)
                .collect { result ->
                    when (result) {
                        is Results.Success -> {
                            _uiState.value = UiState.Success(true)
                        }
                        is Results.Error -> {
                            _uiState.value = UiState.Error("")
                            null
                        }
                    }
                }
        }
    }
}
