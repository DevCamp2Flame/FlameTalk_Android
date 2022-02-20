package com.sgs.devcamp2.flametalk_android.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthReq
import com.sgs.devcamp2.flametalk_android.data.model.auth.AuthRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.additionaluserinfo.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/22
 */

@HiltViewModel
class AdditionalUserInfoViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private var _signUpName = MutableStateFlow<String?>("")
    var signUpName = _signUpName.asStateFlow()

    private var _signUpBirth = MutableStateFlow<String?> ("")
    var signUpBirth = _signUpBirth.asStateFlow()

    private var _signUpTel = MutableStateFlow<String?> ("")
    var signUpTel = _signUpTel.asStateFlow()

    private var _signUpPassword = MutableStateFlow("")
    var signUpPassword = _signUpPassword.asStateFlow()

    private var _signUpEmail = MutableStateFlow("")
    var signUpEmail = _signUpEmail.asStateFlow()

    private var _signUpUiState = MutableStateFlow<UiState<AuthRes>>(UiState.Loading)
    var signUpUiState = _signUpUiState.asStateFlow()

    val TAG: String = "로그"

    init {
    }
    fun updateName(name: String) {
        _signUpName.value = name
        Log.d(TAG, "_signUpName - ${_signUpName.value} called")
    }
    fun updateBirth(birth: String) {
        _signUpBirth.value = birth
        Log.d(TAG, "_signUpBirth - ${_signUpBirth.value} called")
    }
    fun updateTel(tel: String) {
        _signUpTel.value = tel
        Log.d(TAG, "_signUpTel - ${_signUpTel.value} called")
    }
    fun updatePassword(password: String) {
        _signUpPassword.value = password
        Log.d(TAG, "_signUpTel - ${_signUpTel.value} called")
    }
    fun updateEmail(email: String) {
        _signUpEmail.value = email
    }

    fun submitAdditionalUserData(deviceId: String) {
        val authReq = AuthReq(
            _signUpEmail.value, _signUpPassword.value, _signUpName.value!!, _signUpTel.value!!, _signUpBirth.value!!, "GOOGLE", "82", "KR",
            deviceId
        )

        viewModelScope.launch {
            signUpUseCase.invoke(authReq).collect {
                result ->
                when (result) {
                    is Results.Success ->
                        {
                            _signUpUiState.value = UiState.Success(result.data)
                        }
                }
            }
        }
    }
}
