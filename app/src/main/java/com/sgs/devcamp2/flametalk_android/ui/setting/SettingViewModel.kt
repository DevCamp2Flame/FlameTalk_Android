package com.sgs.devcamp2.flametalk_android.ui.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgs.devcamp2.flametalk_android.network.repository.SignRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signRepository: Lazy<SignRepository>
) : ViewModel() {

    // 탈퇴 성공 여부
    private val _isLeave = MutableStateFlow(false)
    val isLeave = _isLeave.asStateFlow()

    // 통신 결과 메세지
    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    fun leaveUser() {
        viewModelScope.launch {
            try {
                val response = signRepository.get().leaveUser()
                _message.value = response.message
                if (response.status == 200)
                    _isLeave.value = true

                Timber.d("$TAG Success Response: $response")
            } catch (ignored: Throwable) {
                _message.value = "알 수 없는 에러 발생"
                Timber.d("$TAG Fail Response: $ignored")
            }
        }
    }

    companion object {
        final const val TAG = "SigninViewModel"
    }
}
