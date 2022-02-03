package com.sgs.devcamp2.flametalk_android.data.model.chat

import kotlinx.serialization.Serializable

/**
 * @author boris
 * @created 2022/01/27
 */

@Serializable
data class ChatReq(
    private val type: MessageType,
    val room_id: String,
    val sender_id: String,
    val nickname: String,
    val contents: String,

)
enum class MessageType {
    ENTER, TALK
}