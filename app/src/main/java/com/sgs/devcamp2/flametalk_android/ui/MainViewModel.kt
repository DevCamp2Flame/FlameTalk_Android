package com.sgs.devcamp2.flametalk_android.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.ConnectWebSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.*
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/31
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectionWebSocket: ConnectWebSocketUseCase,
) : ViewModel() {
    val TAG: String = "로그"
    private var _session = MutableStateFlow<UiState<StompSession>>(UiState.Loading)
    val session = _session.asStateFlow()

    init {
    }
    fun getDeviceToken(context: Context) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                Log.d(TAG, "MainActivityViewModel - $task() called")
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result
                val pref = context.getSharedPreferences("deviceToken", Context.MODE_PRIVATE)
                val editor = pref?.edit()
                editor?.putString("deviceToken", token)?.apply()
                editor?.commit()
            }
        )
    }
    /**
     * Websocket의 연결상태에 따라 Uisate Update하는 function 입니다.
     */
    fun connectChatServer() {
        _session.value = UiState.Loading
        viewModelScope.launch {
            connectionWebSocket.invoke().collect {
                if (it != null) {
                    _session.value = UiState.Success(it)
                } else {
                    _session.value = UiState.Error("세션을 맺지 못했습니다.")
                }
            }
        }
    }
}
