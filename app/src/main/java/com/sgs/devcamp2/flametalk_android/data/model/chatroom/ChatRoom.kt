package com.sgs.devcamp2.flametalk_android.data.model.chatroom

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author boris
 * @created 2022/01/27
 */
@Entity(tableName = "chatroom")
data class ChatRoom(

    @PrimaryKey val room_id: String,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "is_open") val is_open: Boolean,
    @NonNull @ColumnInfo(name = "user_size") val user_size: Int,
)
