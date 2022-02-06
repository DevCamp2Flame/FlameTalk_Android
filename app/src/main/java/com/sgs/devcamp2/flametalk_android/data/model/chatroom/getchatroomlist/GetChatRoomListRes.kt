package com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist

/**
 * @author boris
 * @created 2022/02/05
 */
data class GetChatRoomListRes(
    val userId: String,
    val userChatrooms: List<UserChatRoom>
)
data class UserChatRoom(
    val id: Long,
    val title: String,
    val thumbnail: List<String>,
    val lastReadMessageId: String,
    val inputLock: Boolean,
    val count: Int
)
