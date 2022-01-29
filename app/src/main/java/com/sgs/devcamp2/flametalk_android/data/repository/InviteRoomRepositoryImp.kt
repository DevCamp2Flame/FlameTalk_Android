package com.sgs.devcamp2.flametalk_android.data.repository

import android.util.Log
import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.mapper.mapperToChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomReq
import com.sgs.devcamp2.flametalk_android.data.model.inviteRoom.InviteRoomRes
import com.sgs.devcamp2.flametalk_android.data.source.local.database.AppDatabase
import com.sgs.devcamp2.flametalk_android.data.source.remote.api.InviteRoomApi
import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import com.sgs.devcamp2.flametalk_android.domain.repository.InviteRoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/28
 */
class InviteRoomRepositoryImp @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val inviteRoomApi: InviteRoomApi,
    private val db: AppDatabase
) : InviteRoomRepository {

    val TAG: String = "로그"
    override suspend fun createRoom(InviteRoomReq: InviteRoomReq): Flow<Results<ChatRoomsEntity, WrappedResponse<InviteRoomRes>>> {
        return flow {
            val response = inviteRoomApi.createChatRoom(InviteRoomReq)
            if (response.isSuccessful) {
                Log.d(TAG, "InviteRoomRepositoryImp - resposne is Sucessful")
                // 만약에 response 가 status 코드가 200범위일때 참값을 반환
                // 받은 response로 내부 db에서 채팅방 생성
                val body = response.body()!!
                val data = body.data!!
                val chatRoom = ChatRoomsEntity(data.roomId, data.hostId, data.title, data.isOpen, 4)
                val chatRoomDB = mapperToChatRoom(data)
                val localResponse = db.chatRoomDao().insert(chatRoomDB) //index 반환
                Log.d(TAG,"localReposne  - $localResponse() called")
                emit(Results.Success(chatRoom))
            } else {
                emit(Results.Error("에러 발생"))
            }
        }.flowOn(ioDispatcher)
    }
}
