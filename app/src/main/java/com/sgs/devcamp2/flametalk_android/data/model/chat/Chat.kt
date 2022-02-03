package com.sgs.devcamp2.flametalk_android.data.model.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import javax.annotation.Nonnull

/**
 * @author boris
 * @created 2022/02/01
 */
@Entity(
    tableName = "chat",
    foreignKeys = [
        ForeignKey(
            entity = ChatRoom::class,
            parentColumns = arrayOf("room_id"),
            childColumns = arrayOf("room_id"),
            onDelete = ForeignKey.CASCADE

        )
    ]
)
data class Chat(
    @PrimaryKey val message_id: String,
    @Nonnull @ColumnInfo(name = "room_id") val room_id: String,
    @Nonnull @ColumnInfo(name = "sender_id") val sender_id: String,
    @Nonnull @ColumnInfo(name = "contents") val contents: String,
)
