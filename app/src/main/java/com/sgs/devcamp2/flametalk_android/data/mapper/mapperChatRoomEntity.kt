package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroom.GetChatRoomRes
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.ChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.CreateChatRoomEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.GetChatRoomEntity

/**
 * @author boris
 * @created 2022/02/05
 */


fun mapperToChatRoomEntity(createChatRoomRes: CreateChatRoomRes): ChatRoomEntity {
    return ChatRoomEntity(
        createChatRoomRes.chatroomId,
        createChatRoomRes.userChatroomId,
        createChatRoomRes.title,
        emptyList(),
        "",
        false,
        createChatRoomRes.count
    )
}


fun mapperToGetChatRoomEntity(getChatRoomRes: GetChatRoomRes): GetChatRoomEntity {
    if(getChatRoomRes.profileImage.isNullOrEmpty())
    {
        return GetChatRoomEntity(
            profileNickname = getChatRoomRes.profileNickname,
            profileImage = "",
            profiles = getChatRoomRes.profiles,
            files = getChatRoomRes.files
        )
    }
    else {
        return GetChatRoomEntity(
            profileNickname = getChatRoomRes.profileNickname,
            profileImage = getChatRoomRes.profileImage,
            profiles = getChatRoomRes.profiles,
            files = getChatRoomRes.files
        )
    }

}
