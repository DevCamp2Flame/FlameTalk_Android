package com.sgs.devcamp2.flametalk_android.domain.usecase.chatroomlist

import android.util.Log
import com.sgs.devcamp2.flametalk_android.data.repository.ChatRoomListRepository
import com.sgs.devcamp2.flametalk_android.domain.model.ChatRoomList
import com.sgs.devcamp2.flametalk_android.domain.model.Results

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

/**
 * @author boris
 * @created 2022/01/27
 */
class GetChatRoomListUseCase @Inject constructor(private val repository: ChatRoomListRepository) {
    val TAG: String = "로그"

    suspend fun getChatRoomList(user_id: String): Flow<Results<List<ChatRoomList>>> =
        repository.getChatRoomList(user_id).mapLatest {
            Results.Success(it)
        }.catch {
            Log.d(TAG, "GetChatRoomListUseCase - getChatRoomList() called")
        }
}
