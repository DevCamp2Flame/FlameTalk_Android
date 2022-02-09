package com.sgs.devcamp2.flametalk_android.network.response.sign

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/01/26
 * @updated
 * @desc 탈퇴 Response Body
 *
 * [User Leave API]
 * status	    int 	상태 코드
 * message	    String	status 메시지
 * data	        String	탈퇴 성공 메세지
 */

@Keep
data class UserLeaveResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: String
)
