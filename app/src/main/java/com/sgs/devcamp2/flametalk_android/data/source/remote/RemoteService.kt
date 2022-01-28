package com.sgs.devcamp2.flametalk_android.data.source.remote

import com.sgs.devcamp2.flametalk_android.domain.model.ChatRoomList
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

/**
 * @author boris
 * @created 2022/01/27
 */

interface RemoteService {
    suspend fun getChatRoomList(@Path("user_id") user_id: String): Flow<List<ChatRoomList>>
}
