package com.sgs.devcamp2.flametalk_android.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatReq
import com.sgs.devcamp2.flametalk_android.data.model.chat.MessageType
import com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity.ConnectWebSocketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.serializer
import org.hildan.krossbow.stomp.*
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.conversions.kxserialization.withJsonConversions
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/31
 */
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val connectionWebSocket: ConnectWebSocketUseCase
) : ViewModel() {
    val TAG: String = "로그"
    // lateinit var _session: StompSession

    private var _session: MutableStateFlow<StompSession>? = null
    val session = _session?.asStateFlow()

    lateinit var _jsonStompSessions: StompSessionWithKxSerialization

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
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

    fun connectChatServer() {
        viewModelScope.launch {
            connectionWebSocket.invoke().collect {
                _session?.value = it
                receivedMessage(it)
            }
        }
    }

    fun receivedMessage(session: StompSession) {
        viewModelScope.launch {
            _jsonStompSessions = session.withJsonConversions()

            val chatReq = ChatReq(MessageType.ENTER, "f4cc1a66-a896-4722-ab6b-b27648f17369", "1", "김행콕", "안드로이드")
            _jsonStompSessions.use {
                s ->
                s.convertAndSend("/pub/chat/message", chatReq, serializer())

                val messages: Flow<ChatReq> = s.subscribe("/sub/chat/room", ChatReq.serializer())
                messages.collect {
                    msg ->
                }
            }
        }
    }
    fun pushMessage(roomId: String) {
        viewModelScope.launch {
            // _session.sendText("/api/echo", "")
            // 서버에서 리스폰스가 있어야하나
        }
    }
}
