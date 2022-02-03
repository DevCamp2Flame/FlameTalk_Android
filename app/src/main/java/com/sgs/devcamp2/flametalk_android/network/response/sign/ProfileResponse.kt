package com.sgs.devcamp2.flametalk_android.network.response.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.sgs.devcamp2.flametalk_android.network.request.sign.ProfileUpdateRequest

/**
 * @author 박소연
 * @created 2022/01/31
 * @desc 프로필 조회 Request Body
 *
 * [Header]
 * statusCode	{statusCode}
 * Location	    api/profile/{profileId}	성공한 경우 생성된 프로필 id
 * [Body]
 * status 	Integer	응답 코드
 * message	String	응답 메세지
 */

@Keep
data class ProfileResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Result
) {
    @Keep
    data class Result(
        @SerializedName("profileId")
        val profileId: Long,
        @SerializedName("imageUrl")
        val imageUrl: String?,
        @SerializedName("bgImageUrl")
        val bgImageUrl: String?,
        @SerializedName("sticker")
        val sticker: List<ProfileUpdateRequest.Sticker>?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("isDefault")
        val isDefault: Boolean,
        @SerializedName("updatedDate")
        val updatedDate: String
    )
}
