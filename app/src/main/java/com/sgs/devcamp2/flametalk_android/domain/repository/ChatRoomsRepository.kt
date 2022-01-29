package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/27
 * usecase에서 사용될 respository 실제 구현체는 data layer에 존재
 */
interface ChatRoomsRepository {

//    fun getChatRoom(): Flow<List<ChatRoom>>
    suspend fun getChatRoomList(): Flow<LocalResults<List<ChatRoomsEntity>>>
}
