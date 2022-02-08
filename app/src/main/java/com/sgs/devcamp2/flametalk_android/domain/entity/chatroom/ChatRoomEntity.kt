package com.sgs.devcamp2.flametalk_android.domain.entity.chatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class ChatRoomEntity(
    val chatroomId: String,
    val userChatroomId: Long,
    val title: String,
    val thumbnail: List<String>,
    val lastReadMessageId: String,
    val inputLock: Boolean,
    val count: Int
)
