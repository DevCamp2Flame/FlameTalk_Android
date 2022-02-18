package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.domain.entity.chat.ChatEntity

/**
 * @author 김현국
 * @created 2022/02/04
 * 채팅 Response를 Chat Data Model 로 변환해주는 Mapper 입니다.
 */

fun mapperToChat(chatRes: ChatRes): Chat {
    return Chat(chatRes.message_id, chatRes.message_type, chatRes.room_id, chatRes.sender_id, chatRes.nickname, chatRes.contents, chatRes.file_url, chatRes.created_at)
}

fun mapperToChatEntity(chat: Chat): ChatEntity {
    return ChatEntity(chat.message_id, chat.message_type, chat.room_id, chat.sender_id, chat.nickname, chat.contents, chat.file_url, "", chat.created_at)
}
