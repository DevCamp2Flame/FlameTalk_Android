package com.sgs.devcamp2.flametalk_android.domain.usecase.inviteroom

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.InviteRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/29
 */
class CreateChatRoomUseCase @Inject constructor(
    private val repository: InviteRoomRepository
) {
    suspend fun invoke(inviteRoomReq: InviteRoomReq):
        Flow<Results<ChatRoomsEntity, WrappedResponse<InviteRoomRes>>> {
        return repository.createRoom(inviteRoomReq)
    }
}
