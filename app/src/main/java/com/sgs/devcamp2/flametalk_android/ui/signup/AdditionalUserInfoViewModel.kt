package com.sgs.devcamp2.flametalk_android.ui.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/22
 */

@HiltViewModel
class AdditionalUserInfoViewModel @Inject constructor() : ViewModel() {
    private var _signUpName = MutableStateFlow<String?>("")
    private var _signUpBirth = MutableStateFlow<String?> ("")
    private var _signUpTel = MutableStateFlow<String?> ("")
    val TAG: String = "로그"
    var signUpName = _signUpName.asStateFlow()
    var signUpBirth = _signUpBirth.asStateFlow()
    var signUpTel = _signUpTel.asStateFlow()
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

    fun submitAdditionalUserData(): Flow<String> = flow {
        // String -> response ?
        // 데이터 업로드
    }
}
