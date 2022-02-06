package com.sgs.devcamp2.flametalk_android.network.request.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.sgs.devcamp2.flametalk_android.data.model.Sticker

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/20
 * @desc 회원가입 Request Body
 *
 * [Header]
 * Method	Parameter	Description
 * Content-Type	application/json
 * token	{token}	access token값
 * [Body]
 * Field	Type	Description	Required
 * userId	String	유저 id	Y
 * imageUrl	String	프로필 사진 url	N
 * bgImageUrl	String	프로필 배경 사진 url	N
 * sticker	JSON	프로필에 사용된 스티커 리스트. sticker 참고	N
 * description	String	프로필 상태 메세지	N
 * isDefault	boolean	기본 프로필 여부	Y
 *
 * [sticker]
 * Field	Type	Description	Required
 * stickerId	Integer	스티커 id	Y
 * positionX	Double	스티커 x 좌표	Y
 * positionY	Double	스티커 y 좌표	Y
*/

@Keep
data class ProfileCreateRequest(
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