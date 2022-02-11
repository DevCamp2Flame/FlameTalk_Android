package com.sgs.devcamp2.flametalk_android.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatReq
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.ConnectWebSocketUseCase
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.SaveReceivedMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.*
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
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
    lateinit var _jsonStompSessions: StompSessionWithKxSerialization

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

    fun pushMessage(messageType: String, roomId: String, session: StompSession, contents: String) {
        viewModelScope.launch {
            // sender id -> user_id 로 변경하고
            // nickname도 변경
            _jsonStompSessions = session.withJsonConversions()
            // 만약 message type이 INVITE일 경우 한번 더 메세지를 보내자.
            val chatReq = ChatReq(messageType, roomId, "1643986912282658350", "닉네임", contents, null)
            _jsonStompSessions.convertAndSend("/pub/chat/message", chatReq, ChatReq.serializer())
        }
    }

}
