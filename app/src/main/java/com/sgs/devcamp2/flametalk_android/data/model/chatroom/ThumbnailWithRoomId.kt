package com.sgs.devcamp2.flametalk_android.data.model.chatroom

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author boris
 * @created 2022/02/08
 */
data class ThumbnailWithRoomId(
    @Embedded val room: ChatRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "room_id"
    )
    val thumbnailList: List<Thumbnail>
)
