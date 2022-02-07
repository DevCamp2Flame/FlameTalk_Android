package com.sgs.devcamp2.flametalk_android.ui.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.network.repository.SignRepository
import com.sgs.devcamp2.flametalk_android.network.request.sign.SignupRequest
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

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

    // 유저 닉네임
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname?.asStateFlow()

    // 이메일 체크 성공여부
    private val _emailCheck = MutableStateFlow(false)
    val emailCheck = _emailCheck.asStateFlow()

    // TODO: StateFlow -> Single StateFlow Event
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

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

                when (response.status) {
                    201 -> {
                        _nickname?.value = response.data.nickname
                    }
                    409 -> {
                        _nickname?.value = response.data.nickname
                        _message.value = response.message
                    }
                    else -> {
                        _message.value = response.message
                    }
                }

                Timber.d("Success: $response")
            } catch (ignored: Throwable) {
                Timber.d("Fail: $ignored")
            }
        }.also { messageCleanup() }
    }

    // 이메일 체크 통신
    fun emailCheck(email: String) {
        viewModelScope.launch {
            try {
                val response = signRepository.get().emailCheck(email)

                if (response.status == 200) {
                    _message.value = "유효한 이메일입니다."
                    _emailCheck.value = true
                } else {
                    _message.value = response.message
                    _emailCheck.value = false
                }
                Timber.d("$TAG Success Response: $response")
            } catch (ignored: Throwable) {
                _error.value = "알 수 없는 에러 발생"
                Timber.d("$TAG Success Response: $_error")
            }
        }.also { messageCleanup() }
    }

    private fun messageCleanup() {
        _message.value = ""
    }

    companion object {
        final const val TAG = "SignupViewModel"
    }
}
