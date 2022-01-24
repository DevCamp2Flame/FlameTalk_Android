package com.sgs.devcamp2.flametalk_android.ui.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.network.repository.SignRepository
import com.sgs.devcamp2.flametalk_android.network.request.sign.SigninRequest
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SigninViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signRepository: Lazy<SignRepository>
) : ViewModel() {

    // 닉네임
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    // 프로필 상태메세지
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    fun signIn(email: String, password: String, social: String, deviceId: String) {
        viewModelScope.launch {
            val request = SigninRequest(
                email, password, social, deviceId
            )
            val response = signRepository.get().signin(request)
            // _nickname.value = response.nickname
            Timber.d("Signin Response: $response")
//            try {
//                val request = SigninRequest(
//                    email, password, social, deviceId
//                )
//                val response = signRepository.get().signin(request)
//                // _nickname.value = response.nickname
//                Timber.d("Signin Response: $response")
//            } catch (ignored: Throwable) {
//                // _error.value = "알 수 없는 에러 발생"
//                Timber.d("Signin Response error: $_error")
//            }
        }
    }
}
