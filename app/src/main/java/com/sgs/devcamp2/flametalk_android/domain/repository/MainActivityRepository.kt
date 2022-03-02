package com.sgs.devcamp2.flametalk_android.domain.repository

import kotlinx.coroutines.flow.Flow
import org.hildan.krossbow.stomp.StompSession

/**
 * @author 김현국
 * @created 2022/01/31
 */
interface MainActivityRepository {
    suspend fun connectWebSocket(): Flow<StompSession?>

}
