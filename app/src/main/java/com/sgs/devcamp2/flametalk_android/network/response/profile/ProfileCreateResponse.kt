package com.sgs.devcamp2.flametalk_android.network.response.profile

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.sgs.devcamp2.flametalk_android.data.model.Sticker

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/20
 * @desc 프로필 생성 Request Body
 *
 * [Header]
 * statusCode	{statusCode}
 * Location	    api/profile/{profileId}	성공한 경우 생성된 프로필 id
 * [Body]
 * code	    Integer	응답 코드
 * message	String	응답 메세지
 * id   	Long	프로필 id
 */

@Keep
data class ProfileCreateResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("bgImageUrl")
    val bgImageUrl: String?,
    @SerializedName("sticker")
    val sticker: List<Sticker>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("isDefault")
    val isDefault: Boolean
)
