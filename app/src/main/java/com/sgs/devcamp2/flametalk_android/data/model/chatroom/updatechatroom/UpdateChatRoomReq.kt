package com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class UpdateChatRoomReq(
    val inputLock: Boolean,
    val title: String?,
    val imageUrl: String?
)
