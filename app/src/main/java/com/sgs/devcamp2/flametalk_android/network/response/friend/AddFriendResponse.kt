package com.sgs.devcamp2.flametalk_android.network.response.friend

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 친구 추가 Resposne
 *
 * [Body]
 * code	    Integer	응답 코드
 * message	String	응답 메세지
 * data	    Object	data 참고
 * [Data]
 * friendId	    Long	친구 id
 * nickname	    String	친구 유저의 닉네임
 * profileId	Long	친구 유저의 오픈 프로필 id
 * imageUrl	    String	친구 유저의 프로필 사진 S3 URL
 * description	String	친구 유저의 상태 메세지
 */

@Keep
data class AddFriendResponse(
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
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("profileId")
        val profileId: Long,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("description")
        val description: String
    )
}
