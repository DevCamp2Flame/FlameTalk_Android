package com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom

/**
 * @author boris
 * @created 2022/02/05
 */
data class CreateChatRoomReq(
    val hostId: String,
    val hostOpenProfileId: String?,
    val isOpen: Boolean,
    val users: List<String>,
    val title: String?,
    val thumbnail: String?
)