package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoomUpdate
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes

/**
 * @author 김현국
 * @created 2022/02/08
 * ChatRoom data model로 resposne 를 변환해주는 Mapper입니다.
 */
fun mapperToChatRoomModel(isOpen: Boolean, i: Int, getChatRoomListRes: GetChatRoomListRes): ChatRoom {

    return ChatRoom(
        getChatRoomListRes.userChatrooms[i].chatroomId,
        getChatRoomListRes.userChatrooms[i].userChatroomId,
        getChatRoomListRes.userChatrooms[i].title,
        isOpen,
        getChatRoomListRes.userChatrooms[i].lastReadMessageId,
        getChatRoomListRes.userChatrooms[i].inputLock,
        getChatRoomListRes.userChatrooms[i].count,
        0,
        "",
        System.currentTimeMillis(),
        System.currentTimeMillis()
    )
}
fun mapperToChatRoomModel(createChatRoomRes: CreateChatRoomRes): ChatRoom {
    return ChatRoom(
        id = createChatRoomRes.chatroomId,
        userChatroomId = createChatRoomRes.userChatroomId,
        title = createChatRoomRes.title,
        isOpen = createChatRoomRes.isOpen,
        lastReadMessageId = null,
        inputLock = false,
        count = createChatRoomRes.count,
        0,
        "",
        created_at = System.currentTimeMillis(),
        updated_at = System.currentTimeMillis()
    )
}
fun mapperToChatRoomUpdateModel(chatRes: ChatRes): ChatRoomUpdate {
    return ChatRoomUpdate(
        chatRes.room_id,
        chatRes.contents,
        0,
        lastReadMessageId = chatRes.message_id,
        updated_at = System.currentTimeMillis()
    )
}
