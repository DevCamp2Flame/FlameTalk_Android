package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.domain.repository.MainActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
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
    override suspend fun connectWebSocket(): Flow<StompSession> {
        return flow {
            val connection = stompClient.connect(WEB_SOCKET_URL)
            emit(connection)
        }.flowOn(ioDispatcher)
    }

    companion object {
        val WEB_SOCKET_URL = "ws://10.99.30.180:8080/stomp/chat/websocket"
    }
}
