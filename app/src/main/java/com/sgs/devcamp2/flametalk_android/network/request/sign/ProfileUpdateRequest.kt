package com.sgs.devcamp2.flametalk_android.network.request.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/20
 * @desc 프로필 수정 Request Body
 *
 * [Header]
 * Content-Type	application/json
 * token	    {token}     access token값
 * [Body]
 * userId	    String	유저 id
 * imageUrl	    String	프로필 사진 url
 * bgImageUrl	String	프로필 배경 사진 url
 * sticker	    JSON	프로필에 사용된 스티커 리스트. sticker 참고
 * description	String	프로필 상태 메세지
 * isDefault	boolean	기본 프로필 여부
 *
 * [sticker]
 * stickerId	Integer	스티커 id
 * positionX	Double	스티커 x 좌표
 * positionY	Double	스티커 y 좌표
*/

@Keep
data class ProfileUpdateRequest(
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
) {
    @Keep
    data class Sticker(
        @SerializedName("stickerId")
        val stickerId: Int,
        @SerializedName("positionX")
        val positionX: Double,
        @SerializedName("positionY")
        val positionY: Double
    )
}
