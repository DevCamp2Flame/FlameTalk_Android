package com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroomlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author boris
 * @created 2022/02/05
 */
@Parcelize
data class GetChatRoomListRes(
    val userId: String,
    val userChatrooms: List<UserChatRoom>
) : Parcelable

@Parcelize
data class UserChatRoom(
    val chatroomId: String,
    val userChatroomId: Long,
    val title: String,
    val thumbnail: List<String>,
    val lastReadMessageId: String,
    val inputLock: Boolean,
    val count: Int
) : Parcelable
