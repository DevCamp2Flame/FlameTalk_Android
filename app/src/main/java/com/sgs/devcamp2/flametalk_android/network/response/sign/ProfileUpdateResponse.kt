package com.sgs.devcamp2.flametalk_android.network.response.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/31
 * @desc 회원가입 Request Body
 *
 * [Header]
 * statusCode	{statusCode}
 * Location	    api/profile/{profileId}	성공한 경우 생성된 프로필 id
 * [Body]
 * status 	Integer	응답 코드
 * message	String	응답 메세지
 */

@Keep
data class ProfileUpdateResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)
