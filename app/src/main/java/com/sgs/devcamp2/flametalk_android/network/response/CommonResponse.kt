package com.sgs.devcamp2.flametalk_android.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/05
 * @desc 공통 Response Body
 *
 * [Body]
 * code	    Integer	응답 코드
 * message	String	응답 메세지
 */

@Keep
data class CommonResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)
