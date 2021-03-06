package com.sgs.devcamp2.flametalk_android.data.model.chatroom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author 김현국
 * @created 2022/02/09
 */
@Entity(tableName = "chatroom_update")
data class ChatRoomUpdate(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "messageCount") val messageCount: Int,
    @ColumnInfo(name = "lastReadMessageId")
    val lastReadMessageId: String,
    @ColumnInfo(name = "updated_at") val updated_at: Long
)
