package com.sgs.devcamp2.flametalk_android.data.source.local.dao

import androidx.room.*
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatWithRoomId
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoomUpdate
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.Thumbnail
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ThumbnailWithRoomId
import kotlinx.coroutines.flow.Flow

/**
 * @author 김현국
 * @created 2022/01/27
 * Room database crud 관련
 */
@Dao
interface ChatRoomDao {
    // 채팅방 저장
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(chatroom: ChatRoom): Long

    // 채팅방 리스트 조회
    @Query("SELECT * FROM chatroom WHERE chatroom.isOpen LIKE :isOpen")
    fun getChatRoom(isOpen: Boolean): Flow<List<ThumbnailWithRoomId>>

    // chatroomId에 해당하는 chat 리스트 조회
    @Transaction
    @Query("SELECT * FROM chatroom Where chatroom.id LIKE :chatroomId")
    fun getChatRoomWithId(chatroomId: String): Flow<ChatWithRoomId>

    // 썸네일 저장
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertThumbnail(thumbnail: Thumbnail)

    // 채팅하는 chatRoomId의 썸네일 조회
    @Transaction
    @Query("SELECT * FROM chatroom where chatroom.id LIKE :chatroomId")
    fun getThumbnailWithRoomId(chatroomId: String): Flow<ThumbnailWithRoomId>

    // 마지막으로 읽은 message id 갱신
    @Update(entity = ChatRoom::class)
    fun updateLastReadMessageId(chatroomUpdate: ChatRoomUpdate)

    // 채팅방 삭제
    @Query("DELETE FROM chatroom where userChatroomId Like :userchatroomId")
    fun deleteChatRoomWithuserChatroomId(userchatroomId: Long)

    // 썸네일 제거
    @Transaction
    @Query("DELETE  FROM thumbnail where room_id LIKE :chatroomId")
    fun deleteThumbnailwithRoomId(chatroomId: String)
}
