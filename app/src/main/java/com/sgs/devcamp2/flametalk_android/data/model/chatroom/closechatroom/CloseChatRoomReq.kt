package com.sgs.devcamp2.flametalk_android.data.model.chatroom.closechatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class CloseChatRoomReq(
    val userChatroomId: Long,
    val lastReadMessageId: Long
)
