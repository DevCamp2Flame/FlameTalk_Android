package com.sgs.devcamp2.flametalk_android.data.mapper

import com.sgs.devcamp2.flametalk_android.data.model.chatroom.inviteroom.FriendListRes
import com.sgs.devcamp2.flametalk_android.domain.entity.inviteroom.FriendEntity
import com.sgs.devcamp2.flametalk_android.domain.entity.inviteroom.Preview

/**
 * @author boris
 * @created 2022/02/17
 */
fun mapperToFriendEntity(friendListRes: FriendListRes): FriendEntity {
    val preview = Preview(friendListRes.preview.profileId, friendListRes.preview.imageUrl)
    return FriendEntity(
        friendListRes.friendId, friendListRes.assignedProfileId,
        friendListRes.userId, friendListRes.nickname, preview, "0"
    )
}
