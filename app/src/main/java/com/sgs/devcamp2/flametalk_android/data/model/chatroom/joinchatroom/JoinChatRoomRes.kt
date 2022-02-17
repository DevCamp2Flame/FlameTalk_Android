package com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatroom

/**
 * @author 김현국
 * @created 2022/02/12
 */
data class JoinChatRoomRes(
    val chatroomId: String,
    val userChatroomId: Long,
    val openProfileId: Long,
    val count: Int,
    val isOpen: Boolean,
    val title: String,
    val thumbnail: List<String>
)
