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
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

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

        viewModelScope.launch {
            try {
                val response = signRepository.get().signup(request)
                _nickname.value = response.nickname

                Timber.d("Signup Response: ${response.nickname}")
                Timber.d("Signup Response _nickname: ${_nickname.value}")
            } catch (ignored: Throwable) {
                _error.value = "알 수 없는 에러 발생"
                Timber.d("Signup Response: $_error")
            }
        }
    }
}
