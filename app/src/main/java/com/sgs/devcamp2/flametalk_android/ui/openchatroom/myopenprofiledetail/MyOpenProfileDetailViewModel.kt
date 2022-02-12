package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofiledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofile.GetOpenProfileRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail.DeleteOpenProfileUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenprofiledetail.GetOpenProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/06
 */
@HiltViewModel
class MyOpenProfileDetailViewModel @Inject constructor(
    val deleteOpenProfileUseCase: DeleteOpenProfileUseCase,
    val getOpenProfileUseCase: GetOpenProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<GetOpenProfileRes>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _deleteUiState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val deleteUiState = _deleteUiState.asStateFlow()
    val TAG: String = "로그"
    fun deleteOpenProfile(openProfileId: Long) {
        viewModelScope.launch {
            deleteOpenProfileUseCase.invoke(openProfileId)
                .collect {
                    result ->
                    when (result) {
                        is Results.Success ->
                            {
                                _deleteUiState.value = UiState.Success(result.data)
                            }
                    }
                }
        }
    }

    fun initOpenProfile(openProfileId: Long) {
        viewModelScope.launch {
            getOpenProfileUseCase.invoke(openProfileId)
                .collect {
                    result ->
                    when (result) {
                        is Results.Success ->
                            {
                                _uiState.value = UiState.Success(result.data)
                            }
                    }
                }
        }
    }
}
