package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist

import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/27
 * Domain layer에서 repository를 주입받는다. ( 도메인에서만 import )
 * data layer의 repository를 주입받는다.
 * use case는 entity 즉 view에서만 보일 정보만 필요하다
 * 따라서 return 을 entity로 받고서 mapper를 통해서 data -> entity로 바꿔준다.
 */
class GetChatRoomListUseCase @Inject constructor(
    private val repository: ChatRoomsRepository
) {
    suspend fun invoke(): Flow<LocalResults<List<ChatRoomsEntity>>> {
        return repository.getChatRoomList()
    }
}
