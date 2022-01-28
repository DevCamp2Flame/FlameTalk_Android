package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sgs.devcamp2.flametalk_android.domain.model.ChatRoomList
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/27
 */
@Dao
interface ChatRoomListDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(chatroomList: ChatRoomList)

    @Query("SELECT * FROM chatroom WHERE 'user_id'= :user_id")
    fun getChatRoom(user_id: String): Flow<List<ChatRoomList>>
}
