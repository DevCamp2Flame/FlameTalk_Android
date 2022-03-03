package com.sgs.devcamp2.flametalk_android.domain.entity.chatroom

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.getchatroom.Profile

/**
 * @author 김현국
 * @created 2022/02/05
 */
data class GetChatRoomEntity(
    val profileNickname: String,
    val profileImage: String,
    val profiles: List<Profile>,
    val files: List<String>
)
