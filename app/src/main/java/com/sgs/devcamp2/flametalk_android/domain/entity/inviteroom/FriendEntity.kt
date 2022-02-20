package com.sgs.devcamp2.flametalk_android.domain.entity.inviteroom

/**
 * @author boris
 * @created 2022/02/17
 */

data class FriendEntity(
    val friendId: Long,
    val assignedProfileId: Long,
    val userId: String,
    val nickname: String,
    val preview: Preview,
    var selected: String
)

data class Preview(
    val profileId: Long,
    val imageUrl: String?,

)
