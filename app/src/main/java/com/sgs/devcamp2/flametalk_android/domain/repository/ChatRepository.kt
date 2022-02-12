package com.sgs.devcamp2.flametalk_android.domain.repository

import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/31
 */
interface ChatRepository {
    fun saveReceivedMessage(chatRes: ChatRes): Flow<Long>
}
