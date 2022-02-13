package com.sgs.devcamp2.flametalk_android.ui.openchatroom.editmyopenprofiledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.OpenProfile
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.updateopenprofile.UpdateOpenProfileReq
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail.UpdateOpenProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/06
 */
@HiltViewModel
class EditMyOpenProfileDetailViewModel @Inject constructor(
    val updateOpenProfileUseCase: UpdateOpenProfileUseCase
) : ViewModel() {
    private val _editMyOpenProfileImage = MutableStateFlow("")
    val editMyOpenProfileImage = _editMyOpenProfileImage.asStateFlow()

    private val _editMyOpenProfileName = MutableStateFlow("")
    val editMyOpenProfileName = _editMyOpenProfileName.asStateFlow()

    private val _editMyOpenProfileDescription = MutableStateFlow("")
    val editMyOpenProfileDescription = _editMyOpenProfileDescription.asStateFlow()

    private val _openProfileId = MutableStateFlow(0L)
    val openProfileId = _openProfileId.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<Boolean>>(UiState.Init)
    val uiState = _uiState.asStateFlow()

    fun updateOpenProfile() {
        val updateOpenProfileReq = UpdateOpenProfileReq(_editMyOpenProfileName.value, _editMyOpenProfileImage.value, _editMyOpenProfileDescription.value)
        viewModelScope.launch {
            updateOpenProfileUseCase.invoke(_openProfileId.value, updateOpenProfileReq).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _uiState.value = UiState.Success(true)
                        }
                }
            }
        }
    }

    fun setBackgroundImage(path: String) {
        if (path != null) {
            _editMyOpenProfileImage.value = path
        }
    }
    fun initOpenProfile(openProfile: OpenProfile) {
        _editMyOpenProfileName.value = openProfile.nickname
        _openProfileId.value = openProfile.openProfileId
        _editMyOpenProfileDescription.value = openProfile.description
        _editMyOpenProfileImage.value = openProfile.imageUrl
    }

    fun updateNickname(input: String) {
        _editMyOpenProfileName.value = input
    }
    fun updateDescription(input: String) {
        _editMyOpenProfileDescription.value = input
    }


}
