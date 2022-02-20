package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/18
 */
class GetUserImageUseCase @Inject constructor(
    val repository: ChatRoomRepository
) {
    fun invoke(userId: String): Flow<LocalResults<String>> {
        return repository.getUserImage(userId)
    }
}
