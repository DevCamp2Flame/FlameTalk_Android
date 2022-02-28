package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.domain.repository.MainActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.hildan.krossbow.stomp.ConnectionTimeout
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.WebSocketConnectionException
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/31
 */
class MainActivityRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val stompClient: StompClient,
) : MainActivityRepository {
    /**
     * krossbow library를 이용하여 connection을 맺는 function 입니다.
     */
    val TAG: String = "로그"
    override suspend fun connectWebSocket(): Flow<StompSession?> {
        return flow {
            lateinit var connection: StompSession

            try {
                connection = stompClient.connect(WEB_SOCKET_URL)
                emit(connection)
            } catch (e: ConnectionTimeout) {
                emit(null)
            } catch (e: WebSocketConnectionException) {
                emit(null)
            } catch (e: org.hildan.krossbow.websocket.WebSocketConnectionException) {
                emit(null)
            }
        }.flowOn(ioDispatcher)
    }

    companion object {
        val WEB_SOCKET_URL = "ws://10.0.2.2:8085/stomp/chat/websocket"
    }
}
