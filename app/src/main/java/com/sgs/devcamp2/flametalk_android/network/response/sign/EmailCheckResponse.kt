package com.sgs.devcamp2.flametalk_android.network.response.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/26
 * @updated
 * @desc 이메일 중복 확인 Response Body
 *
 * [Email Check API]
 * email	String	가입하고자 하는 이메일
 */

@Keep
data class EmailCheckResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val data: Boolean
)
