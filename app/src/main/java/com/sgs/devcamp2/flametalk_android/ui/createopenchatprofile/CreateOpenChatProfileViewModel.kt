package com.sgs.devcamp2.flametalk_android.ui.createopenchatprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.createopenprofile.CreateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.createopenchatprofile.CreateOpenProfileUseCase
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
    private val createOpenProfileUseCase: CreateOpenProfileUseCase
) : ViewModel() {
    private var _profile_name = MutableStateFlow("")
    val profile_name = _profile_name.asStateFlow()
    private var _profile_description = MutableStateFlow("")
    val profile_description = _profile_description.asStateFlow()
    private var _profile_image = MutableStateFlow("")
    val profile_image = _profile_image.asStateFlow()
    private var _uiState = MutableStateFlow<UiState<Boolean>>(UiState.Init)
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
        // userid shared preferences
        var createOpenChatProfileReq = CreateOpenProfileReq(
            "1643986912282658350",
            _profile_name.value,
            _profile_image.value,
            _profile_description.value
        )
        _uiState.value = UiState.Loading
        viewModelScope.launch {
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

    fun getDefaultUserProfile() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
        }
    }
}
