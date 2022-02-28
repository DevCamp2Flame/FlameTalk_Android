package com.sgs.devcamp2.flametalk_android.domain.usecase.mainactivity

import com.sgs.devcamp2.flametalk_android.domain.repository.MainActivityRepository
import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompSession
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/01/31
 */
class ConnectWebSocketUseCase @Inject constructor(
    private val repository: MainActivityRepository
) {
    suspend fun invoke():
        Flow<StompSession?> {
        return repository.connectWebSocket()
    }
}
