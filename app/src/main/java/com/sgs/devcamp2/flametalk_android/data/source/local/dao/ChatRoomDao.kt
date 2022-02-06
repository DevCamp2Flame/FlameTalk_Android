package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.*
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/27
 * Room database crud 관련
 */
@Dao
interface ChatRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chatroom: ChatRoom): Long

    @Query("SELECT * FROM chatroom")
    fun getChatRoom(): Flow<List<ChatRoom>>

    @Query("DELETE FROM chatroom")
    fun deleteAllChatRoom()
}
