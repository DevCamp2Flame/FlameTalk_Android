package com.sgs.devcamp2.flametalk_android.domain.entity.chat

/**
 * @author 김현국
 * @created 2022/02/09
 */
data class ChatEntity(
    val message_id: String,
    val message_type: String,
    val room_id: String,
    val sender_id: String,
    val nickname: String,
    val contents: String?,
    val file_url: String?,
    val user_Image: String?,
    val created_at: String
)
