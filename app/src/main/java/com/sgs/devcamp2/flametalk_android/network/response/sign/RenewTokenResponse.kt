package com.sgs.devcamp2.flametalk_android.network.response.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/26
 * @updated
 * @desc 토큰 재발급 Response Body
 *
 * [User Login API]
 * status	    int 	상태 코드
 * message	    String	status 메시지
 *
 * userId	    String	이메일
 * nickname	    String	별명
 * status	    String	권한 & 상태
 * accessToken	String	resource server 통신에 활용
 * refreshToken	String	토큰 재발급에 이용됨
 */

@Keep
data class RenewTokenResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Result
) {
    @Keep
    data class Result(
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
}
