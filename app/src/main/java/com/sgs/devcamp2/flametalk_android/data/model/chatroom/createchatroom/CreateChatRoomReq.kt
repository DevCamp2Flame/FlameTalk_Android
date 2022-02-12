package com.sgs.devcamp2.flametalk_android.data.model.chatroom.createchatroom

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author boris
 * @created 2022/02/05
 */
@Parcelize
data class CreateChatRoomReq(
    val hostId: String,
    val hostOpenProfileId: Long?,
    val isOpen: Boolean,
    val users: List<String>,
    val title: String?,
    val thumbnail: String?
) : Parcelable
