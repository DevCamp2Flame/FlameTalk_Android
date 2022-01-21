package com.sgs.devcamp2.flametalk_android.network.response.sign

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/20
 * @desc 회원가입 Response Body
 *
 * [User Create API]
 * email	String	이메일
 * nickname	String	별명
 * phoneNumber	String	휴대폰 번호
 * birthday	String	생년월일 'YYYY-MM-DD'
 * social	String	로그인 타입(LOGIN, GOOGLE)
 * region	String	국가
 * language	String	언어
 */

@Keep
@JsonClass(generateAdapter = true)
data class SignupResponse(
    @Json(name = "email")
    val email: String,
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
    val language: String
)
