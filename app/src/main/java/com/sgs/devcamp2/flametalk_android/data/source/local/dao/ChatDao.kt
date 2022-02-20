package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat

/**
 * @author 김현국
 * @created 2022/02/01
 */
@Dao
interface ChatDao {

    // 채팅 저장
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(chat: Chat): Long

    @Query("Select Chat.contents from Chat WHERE Chat.message_id Like :message_Id")
    fun getContents(message_Id: String): String

    @Query("Select  Chat.message_id from Chat WHERE Chat.room_id Like :room_id ORDER BY Chat.created_at DESC LIMIT 1")
    fun getLastMessageWithRoomId(room_id: String): String
}
