package com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class CreateChatRoomRes(
    val chatroomId: String,
    val userChatroomId: Long,
    val hostId: String,
    val count: Int,
    val isOpen: Boolean,
    val url: String,
    val title: String,
    val thumbnail: List<String>
)
