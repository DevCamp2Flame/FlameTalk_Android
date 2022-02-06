package com.sgs.devcamp2.flametalk_android.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/26
 * @updated
 * @desc Error Response Body
 *
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
data class ErrorResponse(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("error")
    val error: String,
    @SerializedName("trace")
    val trace: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("path")
    val path: String
)
