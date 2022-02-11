package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chat.ChatRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoomUpdate
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom.CreateChatRoomRes
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes

/**
 * @author boris
 * @created 2022/02/08
 */
fun mapperToChatModel(isOpen: Boolean, i: Int, getChatRoomListRes: GetChatRoomListRes): ChatRoom {

    return ChatRoom(
        getChatRoomListRes.userChatrooms[i].chatroomId,
        getChatRoomListRes.userChatrooms[i].userChatroomId,
        getChatRoomListRes.userChatrooms[i].title,
        isOpen,
        getChatRoomListRes.userChatrooms[i].lastReadMessageId,
        getChatRoomListRes.userChatrooms[i].inputLock,
        getChatRoomListRes.userChatrooms[i].count,
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
        updated_at = System.currentTimeMillis()
    )
}
fun mapperToChatRoomUpdateModel(chatRes: ChatRes): ChatRoomUpdate {
    return ChatRoomUpdate(
        chatRes.room_id,
        lastReadMessageId = chatRes.message_id,
        updated_at = System.currentTimeMillis()
    )
}
