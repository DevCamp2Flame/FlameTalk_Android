package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.Query
import androidx.room.Transaction
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatWithRoomId

/**
 * @author boris
 * @created 2022/02/01
 */

interface ChatRoomWithRoomIdDao {
    @Transaction
    @Query("SELECT * FROM chatroom")
    fun getChatData(): List<ChatWithRoomId>
}
