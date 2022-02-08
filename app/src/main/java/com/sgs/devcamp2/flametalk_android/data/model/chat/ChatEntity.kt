package com.sgs.devcamp2.flametalk_android.data.model.chat

import kotlinx.serialization.Serializable

/**
 * @author boris
 * @created 2022/01/27
 */

@Serializable
data class ChatEntity(
    val type: String,
    val sender_id: String,
    val room_id: String,
    val contents: String
)
