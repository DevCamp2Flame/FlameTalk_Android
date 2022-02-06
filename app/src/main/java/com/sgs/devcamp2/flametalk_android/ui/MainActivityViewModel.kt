package com.sgs.devcamp2.flametalk_android.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.ConnectWebSocketUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.SaveReceivedMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.*
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/31
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val connectionWebSocket: ConnectWebSocketUseCase,
    private val saveReceivedMessageUseCase: SaveReceivedMessageUseCase
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
                Log.d(TAG, "deviceToken - $token")
                editor?.commit()
            }
        )
        Log.d(TAG, "MainActivityViewModel - () called")
    }

    fun connectChatServer() {
        _session.value = UiState.Loading
        viewModelScope.launch {
            connectionWebSocket.invoke().collect {
                _session.value = UiState.Success(it)
            }
        }
    }
}
