package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chat.Chat
import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author 김현국
 * @created 2022/02/04
 * 채팅 Response를 Chat Data Model 로 변환해주는 Mapper 입니다.
 */

fun mapperToChat(chatRes: ChatRes): Chat {
    val simpleFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val date: Date = simpleFormat.parse(chatRes.created_at)
    val created_at = date.time
    return Chat(chatRes.message_id, chatRes.message_type, chatRes.room_id, chatRes.sender_id, chatRes.nickname, chatRes.contents, chatRes.file_url, created_at)
}
