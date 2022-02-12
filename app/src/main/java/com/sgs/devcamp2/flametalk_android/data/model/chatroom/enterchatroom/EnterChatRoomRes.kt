package com.sgs.devcamp2.flametalk_android.data.model.chatroom.enterchatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class EnterChatRoomRes(
    val chatroomId: String,
    val userChatRoomId: Long,
    val openProfileId: Long,
    val count: Int,
    val isOpen: Boolean,
    val title: String,
    val thumbnail: List<String>
)
