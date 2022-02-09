package com.sgs.devcamp2.flametalk_android.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 프로필 기본 정보
 *
 * [Body]
 * friendId	    Long	친구 id
 * nickname	    String	친구 유저의 닉네임
 * profileId	Long	친구 유저의 오픈 프로필 id
 * imageUrl	    String	친구 유저의 프로필 사진 S3 URL
 * description	String	친구 유저의 상태 메세지
 */

@Keep
data class FriendPreview(
    @SerializedName("friendId")
    val friendId: Long,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileId")
    val profileId: Long,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("description")
    val description: String
)
