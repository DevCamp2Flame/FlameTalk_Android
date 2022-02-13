package com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomfiles

/**
 * @author 김현국
 * @created 2022/02/05
 */
data class GetChatRoomFilesRes(
    val id: Long,
    val url: String,
    val title: String,
    val extension: String,
    val createdDate: String
)
