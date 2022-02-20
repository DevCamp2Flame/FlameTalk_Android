package com.sgs.devcamp2.flametalk_android.data.model.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import kotlinx.serialization.Serializable
import javax.annotation.Nonnull

/**
 * @author 김현국
 * @created 2022/02/01
 */
@Entity(
    tableName = "chat",
    foreignKeys = [
        ForeignKey(
            entity = ChatRoom::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("room_id"),
            onDelete = ForeignKey.CASCADE

        )
    ]
)
@Serializable
data class Chat(
    @PrimaryKey val message_id: String,
    @Nonnull @ColumnInfo(name = "message_type") val message_type: String,
    @Nonnull @ColumnInfo(name = "room_id") val room_id: String,
    @Nonnull @ColumnInfo(name = "sender_id") val sender_id: String,
    @Nonnull @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "contents") val contents: String?,
    @ColumnInfo(name = "file_url") val file_url: String?,
    @Nonnull @ColumnInfo(name = "created_at") val created_at: Long
)
