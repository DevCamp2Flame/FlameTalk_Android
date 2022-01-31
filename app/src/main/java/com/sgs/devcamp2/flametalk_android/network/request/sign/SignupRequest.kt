package com.sgs.devcamp2.flametalk_android.network.request.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/20
 * @desc 회원가입 Request Body
 *
 * [User Create API]
 * email	String	이메일	                Y
 * password	String	비밀번호                	N
 * nickname	String	별명	                    Y
 * phoneNumber	String	휴대폰 번호	        Y
 * birthday	String	생년월일 'YYYY-MM-DD'	    Y
 * social	String	로그인 타입(LOGIN, GOOGLE)	Y
 * region	String	국가	                    Y
 * language	String	언어	                    Y
 * deviceId	String	기기 uuid	            Y
 */

@Keep
data class SignupRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String?,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("social")
    val social: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("deviceId")
    val deviceId: String
)
