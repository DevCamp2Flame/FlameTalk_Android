package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author 김현국
 * @created 2022/02/10
 */
class GetThumbnailListUseCase @Inject constructor(
    private val repository: ChatRoomRepository
) {
    suspend fun invoke(chatroomId: String): Flow<LocalResults<ThumbnailWithRoomId>> {
        return repository.getThumbnailList(chatroomId)
    }
}
