package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.inviteroom.InviteRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.inviteroom.InviteRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/28
 *  usecase에서 사용될 respository 실제 구현체는 data layer에 존재
 */
interface InviteRoomRepository {
    suspend fun createRoom(InviteRoomReq: InviteRoomReq): Flow<Results<ChatRoomsEntity, WrappedResponse<InviteRoomRes>>>
}
