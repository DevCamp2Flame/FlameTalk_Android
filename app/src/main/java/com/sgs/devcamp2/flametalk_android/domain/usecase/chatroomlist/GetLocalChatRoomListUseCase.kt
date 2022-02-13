package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/10
 */
class GetLocalChatRoomListUseCase @Inject constructor(
    private val repository: ChatRoomRepository
) {
    suspend fun invoke(isOpen: Boolean): Flow<LocalResults<List<ThumbnailWithRoomId>>> {
        return repository.getLocalChatRoomList(isOpen)
    }
}
