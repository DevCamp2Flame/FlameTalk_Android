package com.sgs.devcamp2.flametalk_android.network.response.friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.sgs.devcamp2.flametalk_android.data.model.friend.FriendStatus

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 친구 상태 변경(숨김, 차단) Response
 *
 * [Body]
 * code	    Integer	응답 코드
 * message	String	응답 메세지
 * data	    Object	응답 메세지. data 참고
 */

@Keep
data class FriendStatusResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: FriendStatus
)
