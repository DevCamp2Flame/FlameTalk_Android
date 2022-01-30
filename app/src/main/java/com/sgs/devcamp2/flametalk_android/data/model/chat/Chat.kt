package com.sgs.devcamp2.flametalk_android.data.model.chat

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull

/**
 * @author boris
 * @created 2022/01/27
 */
@Entity
data class Chat(
    @PrimaryKey val message_id: String,
    @Nonnull @ColumnInfo(name = "room_id") val room_id: String,
    @NonNull @ColumnInfo(name = "contents") val contents: String,

)
