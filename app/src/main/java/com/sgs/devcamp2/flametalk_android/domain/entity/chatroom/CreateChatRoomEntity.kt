package com.sgs.devcamp2.flametalk_android.domain.entity.chatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class CreateChatRoomEntity(
    val userChatRoomId: Long,
    val count: Int,
    val inputLock: Boolean,
    val title: String,
)
