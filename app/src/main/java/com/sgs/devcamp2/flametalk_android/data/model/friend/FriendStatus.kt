package com.sgs.devcamp2.flametalk_android.data.model.friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/31
 * @created 2022/02/06
 * @desc 친구 상태 정보 (즐겨찾기, 숨김, 차단)
 *
 * [FriendStatus]
 * friendId	            String	친구 관계 id
 * userId	            String	친구의 유저 id
 * nickname         	String	친구의 유저 닉네임
 * assignedProfileId	Long	친구에게 보여줄 나의 프로필 id
 * type	                String	친구 관계 타입. DEFAULT, MARKED, HIDDEN, BLOCKED 중 하나
 * preview	            Object	친구 유저 프로필. preview 참고
 */

@Keep
data class FriendStatus(
    @SerializedName("friendId")
    val friendId: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("assignedProfileId")
    val assignedProfileId: Long,
    @SerializedName("type")
    val type: String,
    @SerializedName("preview")
    val preview: FriendPreview,
)
