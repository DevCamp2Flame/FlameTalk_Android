package com.sgs.devcamp2.flametalk_android.network.response.friend

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author boris
 * @created 2022/01/13
 *
 * @property id the id of this group
 * @property friend_id the user_id of User table
 * @property nickname the nickname of User table
 */
@Parcelize
data class FriendEntity(
    var id: String,
    var friend_id: String,
    var nickname: String,
    var mark: Int,
    var selected: String = "0",

) : Parcelable
