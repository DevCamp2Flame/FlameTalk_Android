package com.sgs.devcamp2.flametalk_android.data.model.chat

import androidx.room.Embedded
import androidx.room.Relation
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom

/**
 * @author boris
 * @created 2022/02/01
 */

data class ChatWithRoomId(
    @Embedded val room: ChatRoom,
    @Relation(
        parentColumn = "id",
        entityColumn = "room_id"
    )
    val chatList: List<Chat>
)
