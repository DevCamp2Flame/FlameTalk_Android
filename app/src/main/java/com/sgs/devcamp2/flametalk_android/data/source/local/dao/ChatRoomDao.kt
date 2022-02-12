package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.*
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoomUpdate
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
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

    @Query("SELECT * FROM chatroom WHERE chatroom.isOpen LIKE :isOpen")
    fun getChatRoom(isOpen: Boolean): Flow<List<ThumbnailWithRoomId>>

    @Transaction
    @Query("SELECT * FROM chatroom Where chatroom.id LIKE :chatroomId")
    fun getChatRoomWithId(chatroomId: String): Flow<ChatWithRoomId>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertThumbnail(thumbnail: Thumbnail)

    @Transaction
    @Query("SELECT * FROM chatroom where chatroom.id LIKE :chatroomId")
    fun getThumbnailWithRoomId(chatroomId: String): Flow<ThumbnailWithRoomId>

    @Update(entity = ChatRoom::class)
    fun updateLastReadMessageId(chatroomUpdate: ChatRoomUpdate)

    @Transaction
    @Query("DELETE  FROM thumbnail where room_id LIKE :chatroomId")
    fun deleteThumbnailwithRoomId(chatroomId: String)
}
