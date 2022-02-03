package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.inviteroom.InviteRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.ChatRoomsEntity

/**
 * @author boris
 * @created 2022/01/28
 * data layer의 모델과 domain layer 간의 데이터 변환 mapper
 */

fun mapperToChatRoomsList(chatroomList: List<ChatRoomsEntity>): List<ChatRoom> {
    return chatroomList.toList().map {
        ChatRoom(
            it.room_id,
            it.title,
            it.is_open,
            it.user_size
        )
    }
}

fun mapperToChatRoom(inviteRoomRes: InviteRoomRes): ChatRoom {
    return ChatRoom(inviteRoomRes.roomId, inviteRoomRes.title, inviteRoomRes.isOpen, 4)
}

fun mapperToChatEntity(chatRoomList: List<ChatRoom>): List<ChatRoomsEntity> {
    return chatRoomList.toList().map {
        ChatRoomsEntity(
            it.room_id,
            "",
            it.title,
            it.is_open,
            it.user_size
        )
    }
}
