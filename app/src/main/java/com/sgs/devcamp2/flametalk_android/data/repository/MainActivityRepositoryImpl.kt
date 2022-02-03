package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.domain.repository.MainActivityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/31
 */
class MainActivityRepositoryImpl @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val stompClient: StompClient,
    private val db: AppDatabase
) : MainActivityRepository {
    override suspend fun connectWebSocket(): Flow<StompSession> {
        return flow {
            val connection = stompClient.connect(WEB_SOCKET_URL)
            emit(connection)
        }.flowOn(ioDispatcher)
    }

    companion object {
        val WEB_SOCKET_URL = "ws://10.0.2.2:8080/stomp/chat/websocket"
    }
}
