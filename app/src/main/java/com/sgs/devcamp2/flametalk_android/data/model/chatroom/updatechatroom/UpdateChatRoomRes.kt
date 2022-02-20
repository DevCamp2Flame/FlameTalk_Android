package com.sgs.devcamp2.flametalk_android.data.model.chatroom.updatechatroom

/**
 * @author 김현국
 * @created 2022/02/05
 */
data class UpdateChatRoomRes(
    val userChatroomId: Long,
    val title: String,
    val thumbnail: List<String>,
    val inputLock: Boolean
)
