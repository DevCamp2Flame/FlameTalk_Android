package com.sgs.devcamp2.flametalk_android.data.model.chatroom

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

/**
 * @author 김현국
 * @created 2022/02/08
 */
@Entity(
    tableName = "thumbnail",
    foreignKeys = [
        ForeignKey(
            entity = ChatRoom::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("room_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Thumbnail(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "room_id") val room_id: String,
    @ColumnInfo(name = "image") val image: String

)
