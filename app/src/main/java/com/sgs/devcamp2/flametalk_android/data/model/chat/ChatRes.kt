package com.sgs.devcamp2.flametalk_android.data.model.chat

import kotlinx.serialization.Serializable

/**
 * @author boris
 * @created 2022/01/27
 */

@Serializable
data class ChatRes(
    val message_id: String,
    val room_id: String,
    val sender_id: String,
    val nickname: String,
    val contents: String?,
    val file_url: String?,
    val created_at: String,
)
