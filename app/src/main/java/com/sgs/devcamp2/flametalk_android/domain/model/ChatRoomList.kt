package com.sgs.devcamp2.flametalk_android.domain.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author boris
 * @created 2022/01/27
 */
@Entity(tableName = "chatroom")
data class ChatRoomList(

    @PrimaryKey val room_id: String = "",
    @NonNull @ColumnInfo(name = "user_id") val user_id: String = "",
    @NonNull @ColumnInfo(name = "title") val title: String = "",
    @NonNull @ColumnInfo(name = "is_open") val is_open: Int = 0,
    @NonNull @ColumnInfo(name = "user_size") val user_size: Int = 0,
    val image_id: String = ""
)
