package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.UserChatRoom
import com.sgs.devcamp2.flametalk_android.domain.entity.chatroom.ChatRoomEntity

/**
 * @author 김현국
 * @created 2022/02/09
 */
fun mapperToUserChatRoomModel(chatRoomEntity: ChatRoomEntity): UserChatRoom {
    return UserChatRoom(
        chatRoomEntity.chatroomId,
        chatRoomEntity.userChatroomId,
        chatRoomEntity.title,
        chatRoomEntity.thumbnail,
        chatRoomEntity.lastReadMessageId,
        chatRoomEntity.inputLock,
        chatRoomEntity.count
    )
}
