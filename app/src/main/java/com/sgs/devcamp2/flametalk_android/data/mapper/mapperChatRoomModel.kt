package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.ChatRoom
import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist.GetChatRoomListRes

/**
 * @author boris
 * @created 2022/02/08
 */
fun mapperToChatModel(i: Int, getChatRoomListRes: GetChatRoomListRes): ChatRoom {

    return ChatRoom(
        getChatRoomListRes.userChatrooms[i].chatroomId,
        getChatRoomListRes.userChatrooms[i].userChatroomId,
        getChatRoomListRes.userChatrooms[i].title,
        "getChatRoomListRes.userChatrooms[i].lastReadMessageId",
        getChatRoomListRes.userChatrooms[i].inputLock,
        getChatRoomListRes.userChatrooms[i].count,
        System.currentTimeMillis()
    )
}
