package com.sgs.devcamp2.flametalk_android.domain.entity.chatroom

/**
 * @author 김현국
 * @created 2022/02/05
 */
data class CreateChatRoomEntity(
    val chatroomId: String,
    val userChatRoomId: Long,
    val count: Int,
    val inputLock: Boolean,
    val title: String,
)
