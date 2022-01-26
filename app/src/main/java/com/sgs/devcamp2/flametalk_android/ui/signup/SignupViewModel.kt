package com.sgs.devcamp2.flametalk_android.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.network.repository.SignRepository
import com.sgs.devcamp2.flametalk_android.network.request.sign.SigninRequest
import com.sgs.devcamp2.flametalk_android.network.request.sign.SignupRequest
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/25
 * @desc 회원가입과 관련 비즈니스 로직을 담당하는 ViewModel
 */

@HiltViewModel
class SignupViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signRepository: Lazy<SignRepository>,
) : ViewModel() {
    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    // 유저 닉네임
    private val _nickname: MutableStateFlow<String> = MutableStateFlow("")
    val nickname: StateFlow<String> = _nickname

    // 이메일 체크 성공여부
    private val _emailCheck = MutableStateFlow(false)
    val emailCheck = _emailCheck.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    // 회원가입 통신
    fun signUp(
        email: String,
        password: String,
        nickname: String,
        phoneNumber: String,
        birthday: String,
        social: String,
        region: String,
        language: String,
        deviceId: String
    ) {
        val request = SignupRequest(
            email, password, nickname, phoneNumber, birthday, social, region, language, deviceId
        )
        Timber.d("Signup Request: $request")

        viewModelScope.launch {
            try {
                val response = signRepository.get().signup(request)
                _nickname.value = response.nickname

                Timber.d("Signup Response: $response")
            } catch (ignored: Throwable) {
                _error.value = "알 수 없는 에러 발생"
                Timber.d("Signup Response: $_error")
            }
        }
    }

    // 이메일 체크 통신
    fun emailCheck(email: String) {
        viewModelScope.launch {
            try {
                val response = signRepository.get().emailCheck(email)
                _emailCheck.value = response.data

                Timber.d("$TAG Success Response: $response")
            } catch (ignored: Throwable) {
                _error.value = "알 수 없는 에러 발생"
                Timber.d("$TAG Success Response: $_error")
            }
        }
    }

    companion object {
        final const val TAG = "SignupViewModel"
    }
}
