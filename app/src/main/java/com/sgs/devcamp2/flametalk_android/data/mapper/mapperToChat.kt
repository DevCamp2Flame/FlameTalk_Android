package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes

/**
 * @author 김현국
 * @created 2022/02/04
 * 채팅 Response를 Chat Data Model 로 변환해주는 Mapper 입니다.
 */


fun mapperToChat(chatRes: ChatRes): Chat {
    return Chat(chatRes.message_id, chatRes.message_type, chatRes.room_id, chatRes.sender_id, chatRes.nickname, chatRes.contents, chatRes.file_url, System.currentTimeMillis())
}
