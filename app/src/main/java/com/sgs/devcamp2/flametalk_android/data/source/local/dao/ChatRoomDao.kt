package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.*
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import kotlinx.coroutines.flow.Flow

/**
 * @author boris
 * @created 2022/01/27
 * Room database crud 관련
 */
@Dao
interface ChatRoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(chatroom: ChatRoom): Long

    @Query("SELECT * FROM chatroom")
    fun getChatRoom(): Flow<List<ChatRoom>>

    @Transaction
    @Query("SELECT * FROM chatroom Where chatroom.id LIKE :chatroomId")
    fun getChatRoomWithId(chatroomId: String): Flow<ChatWithRoomId>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertThumbnail(thumbnail: Thumbnail)
}
