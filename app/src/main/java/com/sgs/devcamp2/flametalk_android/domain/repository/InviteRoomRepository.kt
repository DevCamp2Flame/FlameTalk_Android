package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/28
 */
interface InviteRoomRepository {
    suspend fun createRoom(InviteRoomReq: InviteRoomReq): Flow<Results<ChatRoomsEntity, WrappedResponse<InviteRoomRes>>>
}
