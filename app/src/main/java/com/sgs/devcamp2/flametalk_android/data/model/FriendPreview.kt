package com.sgs.devcamp2.flametalk_android.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 프로필 기본 정보
 *
 * [FriendPreview]
 * profileId	Long	친구 유저 프로필 id
 * imageUrl	    String	친구 유저 프로필 사진 S3 URL
 * description	String	친구 유저 프로필 상태 메세지
 */

@Keep
data class FriendPreview(
    @SerializedName("profileId")
    val profileId: Long,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("description")
    val description: String?
)
