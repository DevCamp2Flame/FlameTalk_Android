package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChatEntity
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.ChatRoomsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/27
 * domain layer의 repository의 실제 구현체
 */

class ChatRoomListRepositoryImp @Inject constructor(

    private val ioDispatcher: CoroutineDispatcher,
    private val db: AppDatabase
) : ChatRoomsRepository {
    override suspend fun getChatRoomList(): Flow<LocalResults<List<ChatRoomsEntity>>> {
        return flow {
            val response = db.chatRoomDao().getChatRoom()
            //data source 를 entity로 변환
            val mapResposne = mapperToChatEntity(response)
            emit(LocalResults.Success(mapResposne))
        }.flowOn(ioDispatcher)
    }
}
