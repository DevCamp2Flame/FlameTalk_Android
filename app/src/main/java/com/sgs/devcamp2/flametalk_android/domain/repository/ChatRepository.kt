package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.common.WrappedResponse
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import com.sgs.devcamp2.flametalk_android.domain.entity.Results
import kotlinx.coroutines.flow.Flow

/**
 * @author 김현국
 * @created 2022/01/31
 */
interface ChatRepository {
    fun saveReceivedMessage(chatRes: ChatRes): Flow<Long>
    fun getMessageHistory(roomId: String, lastReadMessage: String): Flow<Results<List<ChatRes>, WrappedResponse<List<ChatRes>>>>
    fun getLastReadMessageId(chatroomId: String): Flow<LocalResults<String>>

}
