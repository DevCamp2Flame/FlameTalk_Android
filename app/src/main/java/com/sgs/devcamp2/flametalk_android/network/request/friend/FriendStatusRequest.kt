package com.sgs.devcamp2.flametalk_android.network.request.friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 상태 변경(숨김, 차단) Request
 *
 * [Body]
 * assignedProfileId	Long	친구에게 보여줄 나의 프로필 id
 * isMarked	            Boolean	관심 친구 설정 여부
 * isHidden	            Boolean	숨김 친구 설정 여부
 * isBlocked	        Boolean	차단 친구 설정 여부
 */

@Keep
data class FriendStatusRequest(
    @SerializedName("assignedProfileId")
    val assignedProfileId: Long,
    @SerializedName("isMarked")
    val isMarked: Boolean,
    @SerializedName("isHidden")
    val isHidden: Boolean,
    @SerializedName("isBlocked")
    val isBlocked: Boolean
)
