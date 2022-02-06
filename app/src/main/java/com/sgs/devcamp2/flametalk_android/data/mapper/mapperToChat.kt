package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatEntity

/**
 * @author boris
 * @created 2022/02/04
 */

fun mapperToChat(chatEntity: ChatEntity): Chat {
    return Chat("", chatEntity.room_id, chatEntity.sender_id, "" , chatEntity.contents)
}
