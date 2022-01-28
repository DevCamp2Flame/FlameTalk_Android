package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.domain.model.ChatRoomList
import dagger.Provides
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/27
 */
interface ChatRoomsRepository {
    suspend fun getChatRoomList(user_id: String): Flow<List<ChatRoomList>>
}
