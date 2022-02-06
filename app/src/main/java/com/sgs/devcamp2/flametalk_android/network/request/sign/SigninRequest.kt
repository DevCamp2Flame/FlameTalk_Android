package com.sgs.devcamp2.flametalk_android.network.request.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/20
 * @desc 로그인 Request Body
 *
 * [User Login API]
 * email	String	이메일	                Y
 * password	String	비밀번호	                Y
 * social	String	로그인 타입(LOGIN, GOOGLE)	Y
 * deviceId	String  기기 uuid	            Y
 */

@Keep
data class SigninRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("social")
    val social: String,
    @SerializedName("deviceId")
    val deviceId: String
)
