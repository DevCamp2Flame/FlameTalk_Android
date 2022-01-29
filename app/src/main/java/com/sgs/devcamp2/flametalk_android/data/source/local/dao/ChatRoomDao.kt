package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.LocalResults
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/27
 */
@Dao
interface ChatRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chatroom: ChatRoom): Long

    @Query("SELECT * FROM chatroom")
    fun getChatRoom(): List<ChatRoom>
}
