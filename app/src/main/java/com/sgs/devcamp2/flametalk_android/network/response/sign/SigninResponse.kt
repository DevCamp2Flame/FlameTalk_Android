package com.sgs.devcamp2.flametalk_android.network.response.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/20
 * @desc 로그인 Response Body
 *
 * [User Login API]
 * email	    String	이메일	                Y
 * password 	String	비밀번호                	N
 * nickname    	String	별명	                    Y
 * phoneNumber	String	휴대폰 번호	            Y
 * birthday	    String	생년월일 'YYYY-MM-DD'	    Y
 * social	    String	로그인 타입(LOGIN, GOOGLE)	Y
 * region	    String	국가	                    Y
 * language 	String	언어	                    Y
 * deviceId	    String	기기 uuid	            Y
 */

@Keep
data class SigninResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)

// @JsonClass(generateAdapter = true)
// data class SigninResponse(
//    @Json(name = "userId")
//    val userId: String,
//    @Json(name = "nickname")
//    val nickname: String,
//    @Json(name = "status")
//    val status: String,
//    @Json(name = "accessToken")
//    val accessToken: String,
//    @Json(name = "refreshToken")
//    val refreshToken: String
// )
