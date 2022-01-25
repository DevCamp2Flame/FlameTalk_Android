package com.sgs.devcamp2.flametalk_android.network.response.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/20
 * @desc 회원가입 Response Body
 *
 * [User Create API]
 * userId	String	아이디
 * email	String	이메일
 * nickname	String	별명
 * phoneNumber	String	휴대폰 번호
 * birthday	String	생년월일 'YYYY-MM-DD'
 * social	String	로그인 타입(LOGIN, GOOGLE)
 * region	String	국가
 * language	String	언어
 */

@Keep
data class SignupResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("email")
    val email: String,
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
    val language: String
)
