package com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class UpdateChatRoomRes(
    val userChatroomId: Long,
    val title: String,
    val lastReadMessageId: String,
    val imageUrl: String,
    val inputLock: Boolean
)
