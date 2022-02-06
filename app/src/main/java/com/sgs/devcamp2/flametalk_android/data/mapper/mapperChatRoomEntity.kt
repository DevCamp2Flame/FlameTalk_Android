package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroom.GetChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.ChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.CreateChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.GetChatRoomEntity

/**
 * @author boris
 * @created 2022/02/05
 */

fun mapperToChatRoomEntityList(getChatRoomListRes: GetChatRoomListRes): List<ChatRoomEntity> {
    return getChatRoomListRes.userChatrooms.map {
        ChatRoomEntity(
            it.id,
            it.title,
            it.thumbnail,
            it.lastReadMessageId,
            it.inputLock,
            it.count
        )
    }
}
fun mapperToCreateChatRoomEntity(createChatRoomRes: CreateChatRoomRes): CreateChatRoomEntity {
    return CreateChatRoomEntity(
        userChatRoomId = createChatRoomRes.userChatroomId,
        title = createChatRoomRes.title,
        inputLock = false,
        count = createChatRoomRes.count

    )
}
fun mapperToGetChatRoomEntity(getChatRoomRes: GetChatRoomRes): GetChatRoomEntity {
    return GetChatRoomEntity(
        profileNickname = getChatRoomRes.profileNickname,
        profileImage = getChatRoomRes.profileImage,
        profiles = getChatRoomRes.profiles,
        files = getChatRoomRes.files
    )
}
