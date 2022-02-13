package com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatrom

/**
 * @author 김현국
 * @created 2022/02/12
 */
data class JoinChatRoomReq(
    val chatroomId: String,
    val userId: String,
    val openProfileId: Long
)
