package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroom

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.inviteroom.FriendListRes
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/02/17
 */
class GetFriendListUseCase @Inject constructor(
    val repository: ChatRoomRepository
) {
    fun invoke(isBirth: Boolean, isHidden: Boolean, isBlock: Boolean): Flow<Results<List<FriendListRes>, WrappedResponse<List<FriendListRes>>>> {
        return repository.getFriendUser(isBirth, isHidden, isBlock)
    }
}
