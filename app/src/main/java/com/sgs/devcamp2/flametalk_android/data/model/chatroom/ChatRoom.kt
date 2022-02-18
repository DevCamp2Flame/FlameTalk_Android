package com.sgs.devcamp2.flametalk_android.data.model.chatroom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author 김현국
 * @created 2022/01/27
 * 수정요망
 */
@Entity(tableName = "chatroom")
data class ChatRoom(

    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "userChatroomId") val userChatroomId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "isOpen") val isOpen: Boolean,
    @ColumnInfo(name = "lastReadMessageId") val lastReadMessageId: String?,
    @ColumnInfo(name = "inputLock") val inputLock: Boolean,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "created_at") val created_at: Long?,
    @ColumnInfo(name = "updated_at") val updated_at: Long?

)
