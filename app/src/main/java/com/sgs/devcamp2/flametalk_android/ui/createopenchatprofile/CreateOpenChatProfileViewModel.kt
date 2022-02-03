package com.sgs.devcamp2.flametalk_android.ui.createopenchatprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.createopenchatprofile.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/03
 */
@HiltViewModel
class CreateOpenChatProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private var _profile_name = MutableStateFlow("")
    val profile_name = _profile_name.asStateFlow()
    private var _profile_description = MutableStateFlow("")
    val profile_description = _profile_description.asStateFlow()
    private var _profile_image = MutableStateFlow("")
    val profile_image = _profile_image.asStateFlow()

    private var _uiState = MutableStateFlow< UiState <Boolean>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

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

    fun createOpenProfile() {
        // open 프로필 생성하는 api 호출
    }
    fun getDefaultUserProfile() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            getUserProfileUseCase.invoke()
                .collect {
                    result ->
                    when (result) {
                        is Results.Success ->
                            {

                                _profile_image.value = result.data.imageUrl
                                _uiState.value = UiState.Success(true)
                            }
                        is Results.Error ->
                            {
                                 _uiState.value = UiState.Error("")
                                null
                            }
                    }
                }
        }
    }
}
