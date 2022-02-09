package com.sgs.devcamp2.flametalk_android.network.response.friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.sgs.devcamp2.flametalk_android.data.model.FriendPreview

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 리스트(생일, 숨김, 차단) Response
 *
 * [Body]
 * code	    Integer	응답 코드
 * message	String	응답 메세지
 * data	    Object	응답 메세지. data 참고
 *
 * [Data]
 * friendId	            String	친구 관계 id
 * userId	            String	친구의 유저 id
 * nickname         	String	친구의 유저 닉네임
 * preview	            Object	친구 유저 프로필. preview 참고
 */

@Keep
data class FriendListResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Data
) {
    @Keep
    data class Data(
        @SerializedName("friendId")
        val friendId: Long,
        @SerializedName("userId")
        val userId: String,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("preview")
        val preview: FriendPreview,
    )
}
