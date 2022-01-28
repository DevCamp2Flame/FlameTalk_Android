package com.sgs.devcamp2.flametalk_android.data.repository

import com.sgs.devcamp2.flametalk_android.data.source.local.dao.ChatRoomListDao
import com.sgs.devcamp2.flametalk_android.data.source.remote.RemoteService
import com.sgs.devcamp2.flametalk_android.data.source.remote.service.ChatRoomsService
import com.sgs.devcamp2.flametalk_android.domain.model.ChatRoomList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/27
 * ChatRoomList에 관련된 retrofit service와 room dao를 선언함
 */

class ChatRoomListRepositoryImp @Inject constructor(
    private val chatRoomListDao: ChatRoomListDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val chatRoomsService: ChatRoomsService
) : ChatRoomListRepository, RemoteService {
    //    override fun getChatRoomList(user_id: String): Flow<List<ChatRoomList>> = flow {
//        emit(chatRoomListService.getChatRoomList(user_id))
//    }.flowOn(ioDispatcher)
    override fun insert(chatroomList: ChatRoomList) {
        return chatRoomListDao.insert(chatroomList)
    }
    override fun getChatRoom(user_id: String): Flow<List<ChatRoomList>> {
        return chatRoomListDao.getChatRoom(user_id).flowOn(ioDispatcher)
    }
    override suspend fun getChatRoomList(user_id: String): Flow<List<ChatRoomList>> {
        return chatRoomsService.getChatRoomList(user_id).flowOn(ioDispatcher)
    }
}
