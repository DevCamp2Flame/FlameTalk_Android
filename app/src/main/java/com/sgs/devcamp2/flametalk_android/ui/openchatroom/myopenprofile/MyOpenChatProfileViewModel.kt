package com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.openprofile.getopenprofilelist.OpenProfile
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.myopenchatprofile.GetOpenProfileListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/26
 */
@HiltViewModel
class MyOpenChatProfileViewModel @Inject constructor(
    private val getOpenProfileListUseCase: GetOpenProfileListUseCase
) : ViewModel() {
    val TAG: String = "로그"
    private val _uiState = MutableStateFlow<UiState<List<OpenProfile>>>(UiState.Init)
    val uiState = _uiState.asStateFlow()
    fun initOpenProfile() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            getOpenProfileListUseCase.invoke().collect { result ->
                when (result) {
                    is Results.Success -> {
                        Log.d(TAG, "MyOpenChatProfileViewModel - ${result.data}")
                        _uiState.value = UiState.Success(result.data.openProfiles)
                    }
                    is Results.Error -> {
                        _uiState.value = UiState.Error("에러 발생")
                    }
                }
            }
        }
    }
}
