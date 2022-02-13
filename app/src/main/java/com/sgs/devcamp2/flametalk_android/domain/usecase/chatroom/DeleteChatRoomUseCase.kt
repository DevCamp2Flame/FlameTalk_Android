package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/07
 */
class DeleteChatRoomUseCase @Inject constructor(
    private val repository: ChatRoomRepository
) {
    suspend fun invoke(userChatRoomId: Long): Flow<Results<Boolean, WrappedResponse<Nothing>>> {
        return repository.leaveChatRoom(userChatRoomId)
    }
}
