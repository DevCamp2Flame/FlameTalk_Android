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
    private val _nickname = MutableStateFlow("") // <String>? = null
    val nickname = _nickname?.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message?.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error?.asStateFlow()

    fun signIn(email: String, password: String, social: String, deviceId: String) {
        viewModelScope.launch {
            try {
                val request = SigninRequest(
                    email, password, social, deviceId
                )
                val response = signRepository.get().signin(request)
                if (response.status == 201) {
                    _nickname.value = response.data.nickname
                } else {
                    _message.value = response.message
                }
            } catch (ignored: Throwable) {
                _error?.value = "알 수 없는 에러 발생"
                Timber.d("Success Response: $_error")
            }
        }
    }
}
