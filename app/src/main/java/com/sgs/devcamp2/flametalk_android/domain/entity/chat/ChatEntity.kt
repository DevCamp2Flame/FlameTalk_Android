package com.sgs.devcamp2.flametalk_android.domain.entity.chat

/**
 * @author boris
 * @created 2022/02/09
 */
data class ChatEntity(
    val messageType: String,
    val room_id: String,
    val sender_id: String,
    val nickname: String,
    val contents: String,
    val file_url: String
)
