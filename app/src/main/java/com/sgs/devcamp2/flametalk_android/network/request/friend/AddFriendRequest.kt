package com.sgs.devcamp2.flametalk_android.network.request.friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 추가 Request
 *
 * [Body]
 * profileId	Long	유저가 친구에게 보여줄 프로필 ID
 * phoneNumber	String	친구로 추가할 유저의 핸드폰 번호. 13 글자 고정
 */

@Keep
data class AddFriendRequest(
    @SerializedName("profileId")
    val profileId: Long,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)
