package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes

/**
 * @author boris
 * @created 2022/02/04
 */

fun mapperToChat(chatRes: ChatRes): Chat {

    return Chat(chatRes.message_id, chatRes.message_type, chatRes.room_id, chatRes.sender_id, chatRes.nickname, chatRes.contents, chatRes.file_url, System.currentTimeMillis())
}
