package com.sgs.devcamp2.flametalk_android.data.model.chatroom.enterchatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class EnterChatRoomReq(
    val chatroomId: String,
    val useId: String,
    val openProfileId: Long?
)
