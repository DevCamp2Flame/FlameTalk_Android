package com.sgs.devcamp2.flametalk_android.network.request.sign

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
@JsonClass(generateAdapter = true)
data class SignupRequest(
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String?,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "phoneNumber")
    val phoneNumber: String,
    @Json(name = "birthday")
    val birthday: String,
    @Json(name = "social")
    val social: String,
    @Json(name = "region")
    val region: String,
    @Json(name = "language")
    val language: String,
    @Json(name = "deviceId")
    val deviceId: String
)
