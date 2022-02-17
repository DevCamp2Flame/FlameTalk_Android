package com.sgs.devcamp2.flametalk_android.data.model.chatroom.joinchatrom

/**
 * @author boris
 * @created 2022/02/17
 */
data class FriendListRes(
    val friendId: Long,
    val assignedProfileId: Long,
    val userId: String,
    val nickname: String,
    val preview: Preview

)
data class Preview(
    val profileId: Long,
    val imageUrl: String,
    val description: String
)
